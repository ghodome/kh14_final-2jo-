package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.AuctionScheduleDao;
import com.art.dto.AuctionScheduleDto;

@CrossOrigin
@RestController
@RequestMapping("/auctionSchedule")
public class AuctionScheduleRestController {
	
	@Autowired
	private AuctionScheduleDao auctionScheduleDao;
	
	//등록
	@PostMapping("/")
	public void insert(@RequestBody AuctionScheduleDto auctionScheduleDto) {
		auctionScheduleDao.insert(auctionScheduleDto);
	}
	
	//목록
	@GetMapping("/")
	public List<AuctionScheduleDto> list() {
		return auctionScheduleDao.selectList();
	}

}
