package com.art.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.art.dao.ChatDao;
import com.art.dto.ChatDto;
import com.art.service.AuctionService2;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketSaveVO;
import com.art.vo.WebSocketSendVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuctionService2 auctionService;
	@Autowired
	private ChatDao chatDao;
	
	@Autowired
	private ChatDao chatDao;
	
	@PatchMapping("/{auctionNo}")
	public WebsocketBidResponseVO bid(@PathVariable int auctionNo,
			@RequestBody WebsocketBidRequestVO request,
			@RequestHeader("Authorization") String token) {
		String memberId=tokenService.check(tokenService.removeBearer(token)).getMemberId();
		WebsocketBidResponseVO response = auctionService.bidProccess(request, auctionNo, memberId);
//		Message<WebsocketBidRequestVO> message =MessageBuilder.withPayload(request).build();
		messagingTemplate.convertAndSend("/auction/everyone",response);
		messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
		return response;
	}
	
	
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

    @MessageMapping("/chat")
    @Transactional
    public void chat(Message<WebSocketSendVO> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader("accessToken");

        if (accessToken == null) {
            return;
        }

        MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
        WebSocketSendVO send = message.getPayload();

        WebSocketSaveVO save = new WebSocketSaveVO();
        save.setContent(send.getContent());
        save.setTime(LocalDateTime.now());
        save.setSender(claimVO.getMemberId());
        save.setLevel(claimVO.getMemberRank());

        messagingTemplate.convertAndSend("/public/chat", save);

        int chatNo = chatDao.sequence();
        ChatDto chatDto = new ChatDto();
        chatDto.setChatNo(chatNo);
        chatDto.setChatSender(claimVO.getMemberId());
        chatDto.setChatReceiver(null);
        chatDto.setChatContent(send.getContent());
        chatDao.insert(chatDto);
    }
}
