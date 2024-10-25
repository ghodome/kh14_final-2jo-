package com.art.restcontroller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.ChatDao;
import com.art.dto.ChatDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketSaveVO;

@CrossOrigin
@RestController
@RequestMapping("/chat")
public class ChatRestController {

	@Autowired
	private ChatDao chatDao;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping("/send")
	@ResponseBody
	public ChatDto insert(
	        @RequestHeader("Authorization") String token,
	        @RequestBody WebSocketSaveVO chatMessage) {
	    // 회원이 관리자 채팅 보내기
	    MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
	    
	    ChatDto chatDto = new ChatDto();
	    chatDto.setChatContent(chatMessage.getContent());

	    // getRank에 따라 sender와 receiver 설정
	    if ("회원".equals(claimVO.getMemberRank())) {
	        chatDto.setChatSender("관리자"); // 회원인 경우 발신자를 관리자 설정
	        chatDto.setChatReceiver("관리자"); // 회원인 경우 수신자를 관리자 설정
	    } else if ("관리자".equals(claimVO.getMemberRank())) {
	        chatDto.setChatSender("회원"); // 관리자인 경우 발신자를 회원 설정
	        chatDto.setChatReceiver("회원"); // 관리자인 경우 수신자를 회원 설정
	    } else {
	        // 기본값 설정 (optional)
	        chatDto.setChatSender(chatMessage.getSender());
	        chatDto.setChatReceiver(chatMessage.getReceiverId());
	    }

	    chatDto.setChatTime(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간 설정

	    chatDao.insert(chatDto);

	    return chatDto; // 직접 반환
	}
}