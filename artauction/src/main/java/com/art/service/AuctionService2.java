package com.art.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.dao.AuctionDao;
import com.art.dto.AuctionDto;
import com.art.error.TargetNotFoundException;
import com.art.vo.AuctionContentVO;
import com.art.vo.WebsocketBidRequestVO;
import com.art.vo.WebsocketBidResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuctionService2 {
	@Autowired
	private TimeService timeService;
	
	@Autowired
	private AuctionDao auctionDao;

	public synchronized WebsocketBidResponseVO bidProccess(WebsocketBidRequestVO request,
			int auctionNo,
			String memberId) {
		String requestTime=timeService.getTime();
		int bidPrice=request.getBid().getBidPrice();
		int hammerPrice=request.getBid().getHammerPrice();
		int bidIncrement=request.getBid().getBidIncrement();
		AuctionDto auctionDto = auctionDao.selectOne(auctionNo);
		if(!(auctionDto.getAuctionBidPrice()==bidPrice)) throw new TargetNotFoundException("응찰가격 불일치");
		AuctionDto auctionNewDto=new AuctionDto();
		int bidNewIncrement;

		if (bidPrice+bidIncrement < 1000000)
		    bidNewIncrement = 50000;
		else if (bidPrice+bidIncrement < 3000000) 
		    bidNewIncrement = 100000;
		else if (bidPrice+bidIncrement < 5000000) 
		    bidNewIncrement = 200000;
		else if (bidPrice+bidIncrement < 10000000) 
		    bidNewIncrement = 500000;
		else if (bidPrice+bidIncrement < 30000000) 
		    bidNewIncrement = 1000000;
		else if (bidPrice+bidIncrement < 50000000) 
		    bidNewIncrement = 2000000;
		else if (bidPrice+bidIncrement < 200000000) 
		    bidNewIncrement = 5000000;
		else 
		    bidNewIncrement = 10000000;
		
		auctionNewDto.setAuctionBidPrice(bidPrice+bidIncrement);
		auctionNewDto.setAuctionBidIncrement(bidNewIncrement);
		auctionNewDto.setAuctionPurchasePrice(auctionDto.getAuctionPurchasePrice());
		auctionNewDto.setAuctionNo(auctionNo);
		log.info("bidDto={}",auctionNewDto);
		if(!auctionDao.update(auctionNewDto)) throw new TargetNotFoundException("응찰 실패");
		
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
}
