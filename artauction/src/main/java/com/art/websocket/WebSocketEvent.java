package com.art.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.art.dao.RoomMessageDao;
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
	private RoomMessageDao roomMessageDao;
	
	// 사용자 세션 정보 저장소
	private Map<String, String> userList = Collections.synchronizedMap(new HashMap<>());
	
	@EventListener // 사용자가 접속할 때
	public void whenUserEnter(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if (accessToken == null) return;
		
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		// 저장소에 사용자 등록
		userList.put(sessionId, claimVO.getMemberId());
		log.info("사용자 접속! 인원수 = {},  세션 = {} , 아이디 = {}", userList.size(), sessionId, claimVO.getMemberId());
	}
	
	@EventListener // 구독 이벤트
	public void whenUserSubscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		if (accessor.getDestination() == null) return;
		
		// 개인 메시지 요청 (1:1 채팅)
		if (accessor.getDestination().startsWith("/private/db/")) {
			String[] pathParts = accessor.getDestination().split("/");
			String senderId = pathParts[2]; // 발신자 ID
			String receiverId = pathParts[3]; // 수신자 ID
			
			// 전달할 정보를 조회
			List<WebsocketMessageVO> messageList = 
				roomMessageDao.selectListDirectMessage(senderId, receiverId);
			
			WebSocketMessageMoreVO moreVO = new WebSocketMessageMoreVO();
			moreVO.setMessageList(messageList);
			moreVO.setLast(messageList.isEmpty());
			
			// 전송
			messagingTemplate.convertAndSend("/private/db/" + senderId + "/" + receiverId, moreVO);
		}
	}
	
	@EventListener // 사용자가 연결을 종료할 때
	public void whenUserLeave(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		
		// 저장소에서 사용자 제거
		userList.remove(sessionId);
		log.info("사용자 접속 종료! 인원수 = {},  세션 = {}", userList.size(), sessionId);
	}
}