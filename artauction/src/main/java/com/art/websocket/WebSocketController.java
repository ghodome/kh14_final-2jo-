package com.art.websocket;

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
import com.art.vo.WebsocketBidResponseVO;
import com.art.vo.WebsocketDealResponseVO;
import com.art.vo.WebsocketRequestVO;

@Controller
public class WebSocketController {
	
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
}
