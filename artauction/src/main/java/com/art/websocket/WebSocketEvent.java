package com.art.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.art.dao.RoomMessageDao;
import com.art.dao.WebsocketMessageDao;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketMessageMoreVO;
import com.art.vo.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class WebSocketEvent {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private WebsocketMessageDao websocketMessageDao;
	
	@Autowired
	private RoomMessageDao roomMessageDao;
	
	//사용자 세션 정보 저장소
	//private Map<String, String> userList = new HashMap<>();//non thread-safe
	//private Map<String, String> userList = new ConcurrentHashMap<>();//thread-safe
	private Map<String, String> userList = Collections.synchronizedMap(new HashMap<>());
	
	@EventListener//implements ApplicationListener<SessionConnectEvent> 효과
	public void whenUserEnter(SessionConnectEvent event) {
		//헤더 정보를 추출
		StompHeaderAccessor accessor = 
								StompHeaderAccessor.wrap(event.getMessage());//분석해!
		String sessionId = accessor.getSessionId();
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if(accessToken == null) return;
		
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		//저장소에 사용자 등록
		userList.put(sessionId, claimVO.getMemberId());
		log.info("사용자 접속! 인원수 = {},  세션 = {} , 아이디 = {}", userList.size(), sessionId, claimVO.getMemberId());
	}
	
	@EventListener//구독 이벤트
	public void whenUserSubscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		if(accessor.getDestination() == null) return;
		
//		if(구독한 채널이 /public/users라면) {
		if("/public/users".equals(accessor.getDestination())) {
			//채널 /users에 전파
			Set<String> values = new TreeSet<>(userList.values());
			messagingTemplate.convertAndSend("/public/users", values);			
		}
		else if(accessor.getDestination().equals("/public/db")) {//비회원
			List<WebsocketMessageVO> messageList = 
					websocketMessageDao.selectListMemberComplete(null, 1, 100);
			if(messageList.isEmpty()) return;
			
			List<WebsocketMessageVO> prevMessageList = 
					websocketMessageDao.selectListMemberComplete(null, 1, 100, messageList.get(0).getNo());
			
			WebSocketMessageMoreVO moreVO = new WebSocketMessageMoreVO();
			moreVO.setMessageList(messageList);
			moreVO.setLast(prevMessageList.isEmpty());
			
			messagingTemplate.convertAndSend("/public/db", moreVO);
		}
		else if(accessor.getDestination().startsWith("/public/db")) {//회원
//			채널명이 /public/db/xxx 일 것이므로 xxx을 원한다면 /public/db/를 제거
			String memberId = accessor.getDestination().substring("/public/db/".length());
//			DB조회 - 이 회원이 볼 수 있는 메세지를 100개 조회하여 전송
//			List<WebsocketMessageDto> messageList = 
//						websocketMessageDao.selectListMember(memberId, 1, 100);
//			List<Object> convertList = messageList.stream()
//					.map(messageDto->{
//						if(messageDto.getWebsocketMessageType().equals("dm")) {//DM
//							WebSocketDMResponseVO response = new WebSocketDMResponseVO();
//							response.setSenderMemberId(messageDto.getWebsocketMessageSender());
//							response.setReceiverMemberId(messageDto.getWebsocketMessageReceiver());
//							response.setContent(messageDto.getWebsocketMessageContent());
//							response.setTime(messageDto.getWebsocketMessageTime().toLocalDateTime());
//							return response;
//						}
//						//일반
//						WebSocketResponseVO response = new WebSocketResponseVO();
//						response.setSenderMemberId(messageDto.getWebsocketMessageSender());
//						response.setContent(messageDto.getWebsocketMessageContent());
//						response.setTime(messageDto.getWebsocketMessageTime().toLocalDateTime());
//						return response;
//					})
//					.collect(Collectors.toList());
//			messagingTemplate.convertAndSend("/public/db/"+memberId, convertList);
			
			List<WebsocketMessageVO> messageList = 
					websocketMessageDao.selectListMemberComplete(memberId, 1, 100);
			if(messageList.isEmpty()) return;
			
			List<WebsocketMessageVO> prevMessageList = 
					websocketMessageDao.selectListMemberComplete(
										memberId, 1, 100, messageList.get(0).getNo());
			
			WebSocketMessageMoreVO moreVO = new WebSocketMessageMoreVO();
			moreVO.setMessageList(messageList);
			moreVO.setLast(prevMessageList.isEmpty());
			
			messagingTemplate.convertAndSend("/public/db/"+memberId, moreVO);
		}
		else if(accessor.getDestination().startsWith("/private/db")) {
			//주소의 형태는 /private/db/방번호/아이디
			String removeStr = accessor.getDestination().substring("/private/db/".length());
			int slash = removeStr.indexOf("/");
			int roomNo = Integer.parseInt(removeStr.substring(0, slash));//슬래시 앞부분
			String memberId = removeStr.substring(slash + 1);//슬래시 뒷부분
			
			//전달할 정보를 조회
			List<WebsocketMessageVO> messageList = 
				roomMessageDao.selectListMemberComplete(memberId, 1, 100, roomNo);
			
			WebSocketMessageMoreVO moreVO = new WebSocketMessageMoreVO();
			moreVO.setMessageList(messageList);
			moreVO.setLast(true);
			if(messageList.size() > 0) {//메세지가 존재한다면
				List<WebsocketMessageVO> prevMessageList = 
						roomMessageDao.selectListMemberComplete(
							memberId, 1, 100, roomNo, messageList.get(0).getNo());
				moreVO.setLast(prevMessageList.isEmpty());
			}
			
			//전송
			messagingTemplate.convertAndSend(
									"/private/db/"+roomNo+"/"+memberId, moreVO);
		}
	}
	
	@EventListener//implements ApplicationListener<SessionDisconnectEvent> 효과
	public void whenUserLeave(SessionDisconnectEvent event) {
		//헤더 정보를 추출
		StompHeaderAccessor accessor = 
								StompHeaderAccessor.wrap(event.getMessage());//분석해!
		String sessionId = accessor.getSessionId();
		
		//저장소에서 사용자 제거
		userList.remove(sessionId);
		log.info("사용자 접속 종료! 인원수 = {},  세션 = {}", userList.size(), sessionId);
		
		//채널 /users에 전파
		Set<String> values = new TreeSet<>(userList.values());
		messagingTemplate.convertAndSend("/public/users", values);
	}
	
}