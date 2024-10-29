package com.art.websocket;

import java.sql.Timestamp;
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
import com.art.vo.WebSocketDMResponseVO;
import com.art.vo.WebSocketRequestVO;
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

    @MessageMapping("/chat/{receiverId}")
    public void dm(@DestinationVariable String receiverId, Message<WebSocketRequestVO> message) {
        log.info("[Chat] receiverId = {}", receiverId);
        
        // 헤더 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader("accessToken");
        if (accessToken == null) {
            return; // 비회원이 채팅을 보냈으면 종료
        }

        // 토큰 해석
        MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));

        // 자신에게 보내는 메시지 차단
        if (claimVO.getMemberId().equals(receiverId)) {
            return;
        }

        // 본문 추출
        WebSocketRequestVO request = message.getPayload();

        // DM 응답 생성
        WebSocketDMResponseVO response = new WebSocketDMResponseVO();
        response.setContent(request.getContent());
        response.setTime(LocalDateTime.now());
        response.setSenderMemberId(claimVO.getMemberId());
        response.setSenderMemberLevel(claimVO.getMemberRank());
        response.setReceiverMemberId(receiverId);

        // DM 전송
        messagingTemplate.convertAndSend("/private/chat/" + response.getSenderMemberId(), response);
        messagingTemplate.convertAndSend("/private/chat/" + response.getReceiverMemberId(), response);

        // DB에 등록
        int chatNo = chatDao.sequence();
        ChatDto chatDto = new ChatDto();
        chatDto.setChatNo(chatNo);
        chatDto.setChatType("chat");
        chatDto.setChatSender(response.getSenderMemberId());
        chatDto.setChatReceiver(response.getReceiverMemberId());
        chatDto.setChatContent(response.getContent());
        chatDto.setChatTime(Timestamp.valueOf(response.getTime()));
        chatDao.insert(chatDto);
    }
}