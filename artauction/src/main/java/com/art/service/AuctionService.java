package com.art.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.dao.AuctionDao;
import com.art.dto.AuctionDto;
import com.art.error.TargetNotFoundException;
import com.art.vo.AuctionContentVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuctionService {
	@Autowired
	private TimeService timeService;
	
	@Autowired
	private AuctionDao auctionDao;

	@Synchronized
	public WebsocketBidResponseVO AuctionProccess(WebsocketBidRequestVO request,
			int auctionNo,
			String memberId) {
		String requestTime=timeService.getTime();
		boolean bidSuccess=false;
		int bidPrice=request.getBid().getBidPrice();
		int hammerPrice=request.getBid().getHammerPrice();
		int bidIncrement=request.getBid().getBidIncrement();
		AuctionDto auctionDto = auctionDao.selectOne(auctionNo);
		if(!(auctionDto.getAuctionBidPrice()==bidPrice)) throw new TargetNotFoundException();
		AuctionDto auctionNewDto=new AuctionDto();
		auctionNewDto.setAuctionBidPrice(bidPrice+bidIncrement);
		int bidNewIncrement;

		if (bidPrice < 300000)
		    bidNewIncrement = bidPrice + 0; 
		else if (bidPrice < 1000000)
		    bidNewIncrement = bidPrice + 50000;
		else if (bidPrice < 3000000) 
		    bidNewIncrement = bidPrice + 100000;
		else if (bidPrice < 5000000) 
		    bidNewIncrement = bidPrice + 200000;
		else if (bidPrice < 10000000) 
		    bidNewIncrement = bidPrice + 500000;
		else if (bidPrice < 30000000) 
		    bidNewIncrement = bidPrice + 1000000;
		else if (bidPrice < 50000000) 
		    bidNewIncrement = bidPrice + 2000000;
		else if (bidPrice < 200000000) 
		    bidNewIncrement = bidPrice + 5000000;
		else 
		    bidNewIncrement = bidPrice + 10000000;
		
		auctionDao.update(auctionNewDto);
		
		if(bidSuccess) {
			WebsocketBidResponseVO response = new WebsocketBidResponseVO();
			AuctionContentVO content=new AuctionContentVO();
			content.setAuctionNo(auctionNo);
			content.setBidPrice(bidPrice);
			content.setBidTime(requestTime);
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
