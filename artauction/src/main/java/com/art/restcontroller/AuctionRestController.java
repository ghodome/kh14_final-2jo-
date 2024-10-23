package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.AuctionDao;
import com.art.dto.AuctionDataCollectionDto;
import com.art.dto.AuctionDto;
import com.art.vo.AuctionLotVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/auction")
public class AuctionRestController {
	@Autowired
	private AuctionDao auctionDao;
	
	@GetMapping("/")
	public List<AuctionDto> list(){
		return auctionDao.selectList();
	}
	@PostMapping("/")
	public void add(@RequestBody AuctionDto auctionDto) {
		auctionDto.setAuctionNo(auctionDao.sequence());
		auctionDto.setAuctionState("예정경매");
		log.info("auctionLot={}",auctionDto.getAuctionLot());
		auctionDao.insert(auctionDto);
	}
	@PatchMapping("/")
	public void update(@RequestBody AuctionDto auctionDto) {
		auctionDao.update(auctionDto);
	}
	@GetMapping("/detail/{auctionNo}")
	public AuctionDto detail(@PathVariable int auctionNo) {
		return auctionDao.selectOne(auctionNo);
	}
	@DeleteMapping("/{auctionNo}")
	public void delete(@PathVariable int auctionNo) {
		auctionDao.delete(auctionNo);
	}
	@GetMapping("/{auctionScheduleNo}")
	public List<AuctionDataCollectionDto> collectionList(@PathVariable int auctionScheduleNo ) {
		return auctionDao.selectDataCollectionList(auctionScheduleNo);
	}
	@GetMapping("/auctionList/{auctionScheduleNo}")
	public List<AuctionLotVO> auctionListWithJoin(@PathVariable int auctionScheduleNo){
		return auctionDao.selectAuctionListWithJoin(auctionScheduleNo);
		
	}
	@GetMapping("/cancelPresent/{auctionNo}")
	public void cancelPresent(@PathVariable int auctionNo) {
		auctionDao.cancelPresent(auctionNo);
	}
	@GetMapping("/uncancelPresent/{auctionNo}")
	public void uncancelPresent(@PathVariable int auctionNo) {
		auctionDao.uncancelPresent(auctionNo);
	}
	
}
