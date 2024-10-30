package com.art.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.WebsocketMessageDao;
import com.art.error.TargetNotFoundException;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.WebSocketMessageMoreVO;
import com.art.vo.WebsocketMessageVO;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class WebSocketRestController {
	
	@Autowired
	private WebsocketMessageDao websocketMessageDao;
	@Autowired
	private TokenService tokenService;
	
	@GetMapping("/more/{firstMessageNo}")
	public WebSocketMessageMoreVO more(
			@RequestHeader(required = false, value = "Authorization") String token,
			@PathVariable int firstMessageNo) {
		String memberId = null;//처음에 비회원이라고 치고
		if(token != null) {//토큰이 있으면 바꿔!
			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
			memberId = claimVO.getMemberId();
		}
		
		//사용자에게 보내줄 목록을 조회
		List<WebsocketMessageVO> messageList = 
				websocketMessageDao.selectListMemberComplete(
								memberId, 1, 100, firstMessageNo);
		if(messageList.isEmpty())
			throw new TargetNotFoundException("보여줄 메세지 없음");
		
		//남은 목록이 더 있는지 확인
		List<WebsocketMessageVO> prevMessageList = 
				websocketMessageDao.selectListMemberComplete(
					memberId, 1, 100, messageList.get(0).getNo());
		
		//반환값 생성
		WebSocketMessageMoreVO moreVO = new WebSocketMessageMoreVO();
		moreVO.setMessageList(messageList);
		moreVO.setLast(prevMessageList.isEmpty());
		
		return moreVO;
	}
}