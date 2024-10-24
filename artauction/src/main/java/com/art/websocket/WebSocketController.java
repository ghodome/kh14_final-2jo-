package com.art.websocket;



import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketSaveVO;
import com.art.vo.WebSocketSendVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {
	//토크
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat")
	public void chat(Message<WebSocketSendVO> message) {
		//헤더 추출
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("accessToken");
//		String refreshToken = accessor.getFirstNativeHeader("refreshToken");
//		log.info("accessToken={}", accessToken);
//		String accessToken = message.getHeaders().get("accessToken", String.class);
//		log.info("headers = {}" ,message.getHeaders());
		
		if(accessToken == null) {
			return;
		}
		
		//토큰 추출
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		WebSocketSendVO send = message.getPayload();
		
		WebSocketSaveVO save = new WebSocketSaveVO();
		save.setContent(send.getContent());
		save.setTime(LocalDateTime.now());
		save.setSender(claimVO.getMemberId());
		save.setLevel(claimVO.getMemberRank());
		
		messagingTemplate.convertAndSend("/public/chat", save);
	}
}
