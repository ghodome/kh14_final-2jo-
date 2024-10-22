package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.AuctionScheduleDao;
import com.art.dto.AuctionScheduleDto;
import com.art.error.TargetNotFoundException;

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
	
	//상세
	@GetMapping("/{auctionScheduleNo}")
	public AuctionScheduleDto detail(@PathVariable int auctionScheduleNo) {
		AuctionScheduleDto auctionScheduleDto = auctionScheduleDao.selectOne(auctionScheduleNo);
		if(auctionScheduleDto == null) {
			throw new TargetNotFoundException();
		}
		return auctionScheduleDto;
	}
	
	//수정
	@PutMapping("/")
	public void update(@RequestBody AuctionScheduleDto auctionScheduleDto) {
		boolean result =  auctionScheduleDao.update(auctionScheduleDto);
		if(result == false) {
			throw new TargetNotFoundException();
		}
	}
	
	//삭제
	@DeleteMapping("/{auctionScheduleNo}")
	public void delete(@PathVariable int auctionScheduleNo) {
		boolean result = auctionScheduleDao.delete(auctionScheduleNo);
		if(result == false) {	//삭제하지 못했다면 200이 아니라 404로 처리
			throw new TargetNotFoundException();
		}
	}
	

}
