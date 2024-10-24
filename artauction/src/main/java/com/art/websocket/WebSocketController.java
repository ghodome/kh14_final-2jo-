package com.art.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.art.service.TimeService;
import com.art.service.TokenService;
import com.art.vo.AuctionContentVO;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketSaveVO;
import com.art.vo.WebSocketSendVO;
import com.art.vo.WebsocketBidResponseVO;
import com.art.vo.WebsocketDealResponseVO;
import com.art.vo.WebsocketRequestVO;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
public class WebSocketController {
	//토크
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private TimeService timeService;
	
	@MessageMapping("/auction/{auctionNo}")
	public void chat(@DestinationVariable int auctionNo,
			Message<WebsocketRequestVO> message) {
		WebsocketRequestVO request = message.getPayload();
		
		StompHeaderAccessor accessor= StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		AuctionContentVO contentVO=new AuctionContentVO();
		String filterStr = request.getContent();
		
		
		contentVO.setContent(filterStr);
		
		if(request.getContent().contains("bid")) {
			WebsocketBidResponseVO response = new WebsocketBidResponseVO();
			
			response.setContent(contentVO);
			response.setSenderMemberId(claimVO.getMemberId());
			response.setSenderMemberRank(claimVO.getMemberRank());
			response.setTime(timeService.getTime());
			messagingTemplate.convertAndSend("/auction/everyone",response);
			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
		}
		else{
			WebsocketDealResponseVO response=new WebsocketDealResponseVO();
			response.setContent(contentVO);
			response.setSenderMemberId(claimVO.getMemberId());
			response.setSenderMemberRank(claimVO.getMemberRank());
			response.setTime(timeService.getTime());
			messagingTemplate.convertAndSend("/auction/everyone",response);
			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
			
		}
	}
		

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
