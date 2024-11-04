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

import com.art.service.AuctionService2;
import com.art.service.TokenService;
import com.art.vo.AuctionContentVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

@RestController
@RequestMapping("/auctionchat")
//@CrossOrigin(origins="http://localhost:3000")
@CrossOrigin
public class WebsocketRestController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuctionService2 auctionService;
	
	@PatchMapping("/{auctionNo}")
	public WebsocketBidResponseVO bid(@PathVariable int auctionNo,
			@RequestBody WebsocketBidRequestVO request,
			@RequestHeader("Authorization") String token) throws ParseException {
		String memberId=tokenService.check(tokenService.removeBearer(token)).getMemberId();
		WebsocketBidResponseVO response = auctionService.bidProccess(request, auctionNo, memberId);
		if(response.isSuccess()) {
			messagingTemplate.convertAndSend("/auction/progress",response);
			messagingTemplate.convertAndSend("/auction/"+auctionNo,response);
		}
		return response;
	}
}
