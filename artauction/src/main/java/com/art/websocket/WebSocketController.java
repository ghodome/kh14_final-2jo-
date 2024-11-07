package com.art.websocket;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.art.dao.WebsocketMessageDao;
import com.art.dto.WebsocketMessageDto;
import com.art.service.AuctionService2;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketDMResponseVO;
import com.art.vo.WebSocketRequestVO;
import com.art.vo.WebSocketResponseVO;
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
	private WebsocketMessageDao websocketMessageDao;
	
//	@PatchMapping("/{auctionNo}")
//	public WebsocketBidResponseVO bid(@PathVariable int auctionNo,
//			@RequestBody WebsocketBidRequestVO request,
//			@RequestHeader("Authorization") String token) throws ParseException {
//		String memberId=tokenService.check(tokenService.removeBearer(token)).getMemberId();
//		WebsocketBidResponseVO response = auctionService.bidProccess(request, auctionNo, memberId);
////		Message<WebsocketBidRequestVO> message =MessageBuilder.withPayload(request).build();
//		messagingTemplate.convertAndSend("/auction/everyone",response);
//		messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
//		return response;
//	}
	
	
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

	@MessageMapping("/chat")//사용자가 /app/chat으로 메세지를 보내면~
	public void chat(Message<WebSocketRequestVO> message) {
		//헤더 추출
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);//도구 생성
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		//String refreshToken = accessor.getFirstNativeHeader("refreshToken");
		if(accessToken == null) {//비회원이 채팅을 보냈으면
			return;//그만둬!
		}
		
		//토큰 해석
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		//본문 추출
		WebSocketRequestVO request = message.getPayload();//메세지 수신
		
		WebSocketResponseVO response = new WebSocketResponseVO();
		response.setContent(request.getContent());//사용자가 보낸 내용 그대로
		response.setTime(LocalDateTime.now());//시간은 현재 시각으로 추가
		response.setSenderMemberId(claimVO.getMemberId());//발신자 회원아이디
		response.setSenderMemberLevel(claimVO.getMemberRank());//발신자의 회원등급
		
		//수동으로 직접 전송
		//messagingTemplate.convertAndSend("채널", 데이터);
		messagingTemplate.convertAndSend("/public/chat", response);
		
		//DB에 등록
		int websocketMessageNo = websocketMessageDao.sequence();
		WebsocketMessageDto websocketMessageDto = new WebsocketMessageDto();
		websocketMessageDto.setWebsocketMessageType("chat");
		websocketMessageDto.setWebsocketMessageNo(websocketMessageNo);
		websocketMessageDto.setWebsocketMessageSender(claimVO.getMemberId());
		websocketMessageDto.setWebsocketMessageReceiver(null);//전체채팅이므로 수신자 없음
		websocketMessageDto.setWebsocketMessageContent(request.getContent());
		websocketMessageDto.setWebsocketMessageTime(Timestamp.valueOf(response.getTime()));//시간 동기화
		websocketMessageDao.insert(websocketMessageDto);
	}
	
//	DM과 관련된 처리
	@MessageMapping("/dm/{receiverId}")
//	@SendTo("/public/dm/{receiverId}")
	public void dm(@DestinationVariable String receiverId,
								Message<WebSocketRequestVO> message) {
		log.info("[DM] receiverId = {}", receiverId);
		//헤더 추출
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);//도구 생성
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		//String refreshToken = accessor.getFirstNativeHeader("refreshToken");
		if(accessToken == null) {//비회원이 채팅을 보냈으면
			return;//그만둬!
		}
		
		//토큰 해석
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		//(+추가) 자신에게 보내는 메세지면 차단(허용도 가능)
		if(claimVO.getMemberId().equals(receiverId)) {
			return;
		}
		
		//본문 추출
		WebSocketRequestVO request = message.getPayload();//메세지 수신
		
		WebSocketDMResponseVO response = new WebSocketDMResponseVO();
		response.setContent(request.getContent());//사용자가 보낸 내용 그대로
		response.setTime(LocalDateTime.now());//시간은 현재 시각으로 추가
		response.setSenderMemberId(claimVO.getMemberId());//발신자 회원아이디
		response.setSenderMemberLevel(claimVO.getMemberRank());//발신자의 회원등급
		response.setReceiverMemberId(receiverId);//수신자 회원아이디
		
		messagingTemplate.convertAndSend("/public/dm/"+response.getSenderMemberId(), response);//발신자
		messagingTemplate.convertAndSend("/public/dm/"+response.getReceiverMemberId(), response);//수신자
		
		//DB에 등록
		int websocketMessageNo = websocketMessageDao.sequence();
		WebsocketMessageDto websocketMessageDto = new WebsocketMessageDto();
		websocketMessageDto.setWebsocketMessageNo(websocketMessageNo);
		websocketMessageDto.setWebsocketMessageType("dm");
		websocketMessageDto.setWebsocketMessageSender(response.getSenderMemberId());
		websocketMessageDto.setWebsocketMessageReceiver(response.getReceiverMemberId());
		websocketMessageDto.setWebsocketMessageContent(response.getContent());
		websocketMessageDto.setWebsocketMessageTime(Timestamp.valueOf(response.getTime()));
		websocketMessageDao.insert(websocketMessageDto);
	}
	
}