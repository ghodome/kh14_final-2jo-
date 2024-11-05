package com.art;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.art.dao.AuctionDao;
import com.art.dao.DealDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class DealTest {
	
	@Autowired
	private AuctionDao auctionDao;
	
	@Autowired
	private DealDao dealDao;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void Test() {
			Timestamp time=new Timestamp(System.currentTimeMillis());
			String fmtTime=dateFormat.format(time);
			List<Integer> auctionNoList=auctionDao.selectListTerminated(fmtTime);

			if(auctionNoList.size()>0) {
				for(Integer auctionNo:auctionNoList) {
					log.info("auctionNo={}",auctionNo);
					auctionDao.changeStateTerminated(auctionNo);
					log.info("result={}",dealDao.insertByAuction(auctionNo));
				}
			}
	}
}
