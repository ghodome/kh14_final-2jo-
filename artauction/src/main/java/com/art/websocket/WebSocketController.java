package com.art.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.service.AuctionService;
import com.art.service.TimeService;
import com.art.service.TokenService;
import com.art.vo.AuctionContentVO;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketSaveVO;
import com.art.vo.WebSocketSendVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;
import com.art.vo.WebsocketDealResponseVO;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping("/auctionws")
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuctionService auctionService;
	
	@PostMapping("/{auctionNo}")
	public WebsocketBidResponseVO bid(@PathVariable int auctionNo,
			@RequestBody WebsocketBidRequestVO request,
			@RequestHeader("Authorization") String token) {
		log.info("bid ={}",request.getBid());
		String memberId=tokenService.check(tokenService.removeBearer(token)).getMemberId();
		WebsocketBidResponseVO response = auctionService.AuctionProccess(request, auctionNo, memberId);
		
		return response;
	}
	
	
	@MessageMapping("/message/{auctionNo}")
	public void chat(@DestinationVariable int auctionNo,
			Message<WebsocketBidRequestVO> message) {
		log.info("메세지 전파 요청 발생");
//		WebsocketBidRequestVO request = message.getPayload();
//		int bidPrice=request.getBid().getBidPrice();
//		int hammerPrice=request.getBid().getHammerPrice();
//		
//		StompHeaderAccessor accessor= StompHeaderAccessor.wrap(message);
//		String accessToken = accessor.getFirstNativeHeader("accessToken");
//		String memberId = tokenService.check(tokenService.removeBearer(accessToken)).getMemberId();
//	
//		AuctionContentVO content=new AuctionContentVO();
//		content.setAuctionNo(auctionNo);
//		content.setBidPrice(bidPrice);
//		content.setBidTime(timeService.getTime());
//		content.setMemberId(memberId.substring(0, 4)+"****");
//		
//		content.setContent(String.valueOf(auctionNo)+"번 경매, 입찰가 : "
//				+hammerPrice+", 입찰자 : "+content.getMemberId());
//		
//		if(request.getType().equals("bid")) {
//			WebsocketBidResponseVO response = new WebsocketBidResponseVO();
//			
//			response.setContent(contentVO);
//			response.setSenderMemberId(claimVO.getMemberId());
//			response.setSenderMemberRank(claimVO.getMemberRank());
//			response.setTime(contentVO.getBidTime());
//			messagingTemplate.convertAndSend("/auction/everyone",response);
//			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
//		}
//		else{
//			WebsocketDealResponseVO response=new WebsocketDealResponseVO();
//			response.setContent(contentVO);
//			response.setSenderMemberId(claimVO.getMemberId());
//			response.setSenderMemberRank(claimVO.getMemberRank());
//			response.setTime(contentVO.getBidTime());
//			messagingTemplate.convertAndSend("/auction/everyone",response);
//			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
//			
//		}
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
