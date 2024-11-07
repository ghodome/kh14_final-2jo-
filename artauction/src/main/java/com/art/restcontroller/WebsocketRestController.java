package com.art.restcontroller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.MemberDao;
import com.art.service.AuctionService2;
import com.art.service.TimeService;
import com.art.service.TokenService;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

@RestController
@RequestMapping("/auctionchat")
@CrossOrigin
public class WebsocketRestController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuctionService2 auctionService;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private TimeService timeService;
	
	@PatchMapping("/{auctionNo}")
	public WebsocketBidResponseVO bid(@PathVariable int auctionNo,
			@RequestBody WebsocketBidRequestVO request,
			@RequestHeader("Authorization") String token) throws ParseException {
		String requestTime=timeService.getTime();
		String memberId=tokenService.check(tokenService.removeBearer(token)).getMemberId();
		long memberPoint=memberDao.selectPoint(memberId);
		if(memberPoint<(request.getBid().getBidPrice()+request.getBid().getBidIncrement())*3/10) {
			WebsocketBidResponseVO response=new WebsocketBidResponseVO();
			response.setSuccess(false);
			return response;
		}
		
		WebsocketBidResponseVO response = auctionService.bidProccess(request, auctionNo, memberId,requestTime);
		if(response.isSuccess()) {
			messagingTemplate.convertAndSend("/auction/progress",response);
			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
		}
		return response;
	}
}
