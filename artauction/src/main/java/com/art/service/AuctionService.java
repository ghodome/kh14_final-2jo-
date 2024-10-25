package com.art.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.error.TargetNotFoundException;
import com.art.vo.AuctionContentVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

@Service
public class AuctionService {
	@Autowired
	private TimeService timeService;
	
	public WebsocketBidResponseVO AuctionProccess(WebsocketBidRequestVO request,
			int auctionNo,
			String memberId) {
		boolean bidSuccess=true;
		if(bidSuccess) {
			int bidPrice=request.getBid().getBidPrice();
			int hammerPrice=request.getBid().getHammerPrice();
			WebsocketBidResponseVO response = new WebsocketBidResponseVO();
			AuctionContentVO content=new AuctionContentVO();
			content.setAuctionNo(auctionNo);
			content.setBidPrice(bidPrice);
			content.setBidTime(timeService.getTime());
			content.setMemberId(memberId.substring(0, 4)+"****");
			content.setContent(String.valueOf(auctionNo)+"번 경매, 입찰가 : "
					+hammerPrice+", 입찰자 : "+content.getMemberId());
			response.setContent(content);
			response.setSuccess(true);
			return response;
		}
		else throw new TargetNotFoundException("응찰 실패");
	}
}
