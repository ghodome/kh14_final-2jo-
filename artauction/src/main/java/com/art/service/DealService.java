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
	
//	@Scheduled(cron="2,32 * * * * *")
//	public void manageTerminate() {
//		Timestamp time=new Timestamp(System.currentTimeMillis());
//		String fmtTime=dateFormat.format(time);
//		List<Integer> auctionNoList=auctionDao.selectListTerminated(fmtTime);
//		log.info("낙찰 스케쥴러 실행"+auctionNoList.size());
//		if(auctionNoList.size()>0) {
//			for(Integer auctionNo:auctionNoList) {
//				log.info("auctionNo={}",auctionNo);
//				auctionDao.changeStateTerminated(auctionNo);
//				log.info("result={}",dealDao.insertByAuction(auctionNo));
//			}
//		}
//	}
	
}
