package com.art.websocket;

import java.util.Collections;
import java.util.HashMap;
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

import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class WebSocketEvent {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	
	private Map<String, String> userList = Collections.synchronizedMap(new HashMap<>());

	@EventListener//접속했을떄
	public void whenUserEnter(SessionConnectEvent event){
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String seesisonId = accessor.getSessionId();
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if(accessToken == null)return;
		
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
	
		userList.put(seesisonId, claimVO.getMemberId());
//		log.info("사용자 접속 세션 = {}, 아이디 = {}, 인원수 = {}, 등급 ={}", seesisonId, claimVO.getMemberId(), userList.size(), claimVO.getMemberRank());
		
		Set<String> values = new TreeSet<>(userList.values());
		messagingTemplate.convertAndSend("/public/users", userList);
	}
	@EventListener//접속종료
	public void whenUserLeave(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String seesisonId = accessor.getSessionId();
		
		userList.remove(seesisonId);
//		log.info("사용자 종료 세션 = {}, 인원수 = {}", seesisonId, userList.size());				
		
		Set<String> values = new TreeSet<>(userList.values());
		messagingTemplate.convertAndSend("/public/users", userList);
		
	}
	
}
