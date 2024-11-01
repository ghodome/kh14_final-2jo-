package com.art.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.art.dao.AuctionDao;
import com.art.dao.BidDao;
import com.art.dao.DealDao;
import com.art.dto.AuctionDto;
import com.art.dto.BidDto;
import com.art.dto.DealDto;
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
	
	@Autowired
	private DealDao dealDao;
	
	@Autowired
	private BidDao bidDao;
	
	@Transactional
	public synchronized WebsocketBidResponseVO bidProccess(WebsocketBidRequestVO request,
			int auctionNo,
			String memberId) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String requestTime=timeService.getTime();
		int bidPrice=request.getBid().getBidPrice();
		int bidIncrement=request.getBid().getBidIncrement();
		int newBidPrice=bidPrice+bidIncrement;
		
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
		
		
		auctionNewDto.setAuctionBidPrice(newBidPrice);
		auctionNewDto.setAuctionBidIncrement(bidNewIncrement);
		auctionNewDto.setAuctionBidCnt(auctionDto.getAuctionBidCnt());
		auctionNewDto.setAuctionNo(auctionNo);
		auctionNewDto.setAuctionSuccessBidder(memberId);
		if(!auctionDao.update(auctionNewDto)) throw new TargetNotFoundException("응찰 실패");
		String contentForSchedule="LOT "+request.getBid().getAuctionLot()+" ["+request.getBid().getWorkName()+"], 입찰가 : "
				+newBidPrice+", 입찰자 : "+memberId;
		String contentForLot="입찰가 : "+newBidPrice+", 입찰자 : "+memberId;
		
		//응찰 내역 저장
		BidDto bidDto=new BidDto();
		bidDto.setAuctionNo(auctionNo);
		bidDto.setBidContent(contentForLot);
		bidDto.setBidPrice(newBidPrice);
		bidDto.setBidTime(fmt.parse(requestTime));
		bidDto.setMemberId(memberId);
		bidDao.insert(bidDto);
		
		
		WebsocketBidResponseVO response = new WebsocketBidResponseVO();
		AuctionContentVO content=new AuctionContentVO();
		content.setAuctionNo(auctionNo);
		content.setBidPrice(bidPrice+bidIncrement);
		content.setBidTime(requestTime);
		content.setMemberId(memberId.substring(0, 4)+"****");
		content.setContentForSchedule(contentForSchedule);
		content.setContentForLot(contentForLot);
		content.setAuctionLot(request.getBid().getAuctionLot());
		response.setContent(content);
		response.setSuccess(true);
		return response;
	}
	
	public void bidToDeal(BidDto bidDto) {
		DealDto dealDto = new DealDto();
		dealDto.setDealBuyer(bidDto.getMemberId());
		dealDto.setBidNo(bidDto.getBidNo());
		dealDto.setDealPrice(bidDto.getBidPrice());
		dealDto.setDealStatus("결제대기");
		dealDto.setDealTime(bidDto.getBidTime());
		dealDao.insert(dealDto);   
	}
}
