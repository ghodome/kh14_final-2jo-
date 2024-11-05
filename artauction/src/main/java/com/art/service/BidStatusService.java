package com.art.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.art.dao.AuctionDao;
import com.art.dao.AuctionScheduleDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidStatusService {
    @Autowired
    private AuctionScheduleDao auctionScheduleDao;
    @Autowired
    private AuctionDao auctionDao;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    @Scheduled(cron = "1,31 0,10,20,30,40,50 * * * *")
//    public void manageProgress() {
//        Timestamp time = new Timestamp(System.currentTimeMillis());
//
//        String formattedTime = dateFormat.format(time);
//        List<Integer> list = auctionScheduleDao.selectListStarted(formattedTime);
//        for (Integer auctionScheduleNo : list) {
//            auctionScheduleDao.statusToProgress(auctionScheduleNo);
//            auctionDao.statusToProgress(auctionScheduleNo);
//            
//        }
//    }
//
//    @Scheduled(cron = "1,31 0,10,20,30,40,50 * * * *")
//    public void manageTermination() {
//        Timestamp time = new Timestamp(System.currentTimeMillis());
//        
//        String formattedTime = dateFormat.format(time);
//
//        List<Integer> list = auctionScheduleDao.selectListTerminated(formattedTime);
//        for (Integer auctionScheduleNo : list) {
//        	log.info("auctionScheduleNo={}",auctionScheduleNo);
//            auctionScheduleDao.statusToTemination(auctionScheduleNo);
//        }
//    }
}
