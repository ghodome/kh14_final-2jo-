package com.art.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.art.dao.AuctionScheduleDao;

@Service
public class BiddingEndService {
	@Autowired
	private AuctionScheduleDao auctionScheduleDao; 
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(cron = "0,30 * * * * *")
	public void manageProgress() {
		Date time = new Date(System.currentTimeMillis());
		List<Integer> list=auctionScheduleDao.selectListStarted(time);
		for(int i=0; i<list.size(); i++) {
			auctionScheduleDao.statusToProgress(list.get(i));
		}
	}
	@Scheduled(cron = "0 * * * * *")
	public void manageTermination() {
		Date time = new Date(System.currentTimeMillis());
		List<Integer> list=auctionScheduleDao.selectListTerminated(time);
		for(int i=0; i<list.size(); i++) {
			auctionScheduleDao.statusToTemination(list.get(i));
		}
	}
}
