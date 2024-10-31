package com.art.restcontroller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.art.dao.RoomDao;
import com.art.dao.RoomMessageDao;
import com.art.dto.RoomMessageDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketRequestVO;
import com.art.vo.WebSocketResponseVO;

@Controller
public class RoomMessageController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RoomMessageDao roomMessageDao;
	@Autowired
	private RoomDao roomDao;

	@MessageMapping("/room/{roomNo}")
	public void chat(@DestinationVariable int roomNo, 
								Message<WebSocketRequestVO> message) {
		//토큰 검사 및 해석
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("accessToken");
		if(accessToken == null) return;
		
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		//검사 추가(선택적 사용)
		//RoomMemberDto roomMemberDto = new RoomMemberDto();
		//roomMemberDto.setMemberId(claimVO.getMemberId());
		//roomMemberDto.setRoomNo(roomNo);
		//boolean canEnter = roomDao.check(roomMemberDto);
		//if(canEnter == false) return;
		
		WebSocketRequestVO request = message.getPayload();//메세지 추출
		
		//메세지 발송
		WebSocketResponseVO response = new WebSocketResponseVO();
		response.setSenderMemberId(claimVO.getMemberId());
		response.setSenderMemberLevel(claimVO.getMemberRank());
		response.setTime(LocalDateTime.now());
		response.setContent(request.getContent());
		messagingTemplate.convertAndSend("/private/chat/"+roomNo, response);
		
		//DB 저장
		int roomMessageNo = roomMessageDao.sequence();
		RoomMessageDto roomMessageDto = new RoomMessageDto();
		roomMessageDto.setRoomMessageNo(roomMessageNo);
		roomMessageDto.setRoomMessageType("chat");
		roomMessageDto.setRoomMessageSender(claimVO.getMemberId());
		roomMessageDto.setRoomMessageReceiver(null);
		roomMessageDto.setRoomMessageContent(request.getContent());
		roomMessageDto.setRoomMessageTime(Timestamp.valueOf(response.getTime()));
		roomMessageDto.setRoomNo(roomNo);//전체 채팅과 다른점
		roomMessageDao.insert(roomMessageDto);
	}
	
}