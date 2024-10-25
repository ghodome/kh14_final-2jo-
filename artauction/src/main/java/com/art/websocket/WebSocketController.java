package com.art.websocket;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.art.dao.ChatDao;
import com.art.dto.ChatDto;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private ChatDao chatDao;

    @MessageMapping("/auction/{auctionNo}")
    public void chat(@DestinationVariable int auctionNo,
                     Message<WebsocketRequestVO> message) {
        WebsocketRequestVO request = message.getPayload();

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader("accessToken");
        MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
        AuctionContentVO contentVO = new AuctionContentVO();
        String filterStr = request.getContent();

        contentVO.setContent(filterStr);
        contentVO.setBidNo(0);
        contentVO.setBidPrice(0);
        contentVO.setBidTime(timeService.getTime());
        contentVO.setMemberId(claimVO.getMemberId());

        if (request.getContent().contains("bid")) {
            WebsocketBidResponseVO response = new WebsocketBidResponseVO();
            response.setContent(contentVO);
            response.setSenderMemberId(claimVO.getMemberId());
            response.setSenderMemberRank(claimVO.getMemberRank());
            response.setTime(contentVO.getBidTime());
            messagingTemplate.convertAndSend("/auction/everyone", response);
            messagingTemplate.convertAndSend("/auction/" + auctionNo, response);
        } else {
            WebsocketDealResponseVO response = new WebsocketDealResponseVO();
            response.setContent(contentVO);
            response.setSenderMemberId(claimVO.getMemberId());
            response.setSenderMemberRank(claimVO.getMemberRank());
            response.setTime(contentVO.getBidTime());
            messagingTemplate.convertAndSend("/auction/everyone", response);
            messagingTemplate.convertAndSend("/auction/" + auctionNo, response);
        }
    }

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
