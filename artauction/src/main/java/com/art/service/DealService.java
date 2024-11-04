package com.art.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.art.dao.AuctionDao;
import com.art.dao.BidDao;
import com.art.dao.DealDao;
import com.art.dto.BidDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealService {
	@Autowired
	private BidDao bidDao;
	
	@Autowired
	private AuctionDao auctionDao;
	
	@Autowired
	private DealDao dealDao;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(cron="1,31 * * * * *")
	public void manageTerminate() {
		Timestamp time=new Timestamp(System.currentTimeMillis());
		String fmtTime=dateFormat.format(time);
		List<Integer> auctionNoList=auctionDao.selectListTerminated(fmtTime);
		if(auctionNoList.size()>0) {
			List<BidDto> bidList=new ArrayList<BidDto>();
			for(Integer auctionNo:auctionNoList) {
				auctionDao.changeStateTerminated(auctionNo);
				bidList.add(bidDao.selectOneByAuctionNo(auctionNo));
			}
			log.info("bidList={}",bidList);
		}
	}
	
	@Scheduled(cron="* * * * * *")
	public void manageDeal() {
		
	}
	
	
}
