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
import com.art.dto.AuctionDto;
import com.art.service.AttachmentService;
import com.art.vo.AuctionLotDetailVO;
import com.art.vo.AuctionLotListVO;
import com.art.vo.AuctionLotVO;
import com.art.vo.AuctionScheduleInfoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/auction")
public class AuctionRestController {
	
	@Autowired
	private AuctionDao auctionDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@GetMapping("/")
	public List<AuctionDto> list(){
		return auctionDao.selectList();
	}
	
	//출품등록
	@PostMapping("/")
	public void add(@RequestBody AuctionDto auctionDto) {
		auctionDto.setAuctionNo(auctionDao.sequence());
		auctionDto.setAuctionState("예정경매");
		int startPrice=auctionDto.getAuctionStartPrice();
		int bidIncrement;
		if (startPrice < 1000000)
			bidIncrement = 50000;
		else if (startPrice < 3000000) 
			bidIncrement = 100000;
		else if (startPrice < 5000000) 
			bidIncrement = 200000;
		else if (startPrice < 10000000) 
			bidIncrement = 500000;
		else if (startPrice < 30000000) 
			bidIncrement = 1000000;
		else if (startPrice < 50000000) 
			bidIncrement = 2000000;
		else if (startPrice < 200000000) 
			bidIncrement = 5000000;
		else 
			bidIncrement = 10000000;
		auctionDto.setAuctionBidIncrement(bidIncrement);
		
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
	
	//출품작삭제
	@DeleteMapping("/{auctionNo}")
	public void delete(@PathVariable int auctionNo) {
		auctionDao.delete(auctionNo);
	}
	
	//출품작불러오기(일정상세페이지)
	@GetMapping("/{auctionScheduleNo}")
	public List<AuctionLotVO> auctionListWithJoin(@PathVariable int auctionScheduleNo){
		return auctionDao.selectAuctionListWithJoin(auctionScheduleNo);
	}
	
	//출품작불러오기(경매목록)
	@GetMapping("/auctionLotList/{auctionScheduleNo}")
	public List<AuctionLotListVO> auctionCollectionListWithJoin(
			@PathVariable int auctionScheduleNo){
		return auctionDao.selectAuctionLotListWithJoin(auctionScheduleNo);
	}
	
	//경매일정 정보 불러오기
	@GetMapping("/auctionScheduleInfo/{auctionScheduleNo}")
	public List<AuctionScheduleInfoVO> auctionScheduleInfo(
			@PathVariable int auctionScheduleNo){
		return auctionDao.selectAuctionScheduleInfo(auctionScheduleNo);
	}
	
	//출품취소
	@GetMapping("/cancelPresent/{auctionNo}")
	public void cancelPresent(@PathVariable int auctionNo) {
		auctionDao.cancelPresent(auctionNo);
	}
	
	//출품재등록
	@GetMapping("/uncancelPresent/{auctionNo}")
	public void uncancelPresent(@PathVariable int auctionNo) {
		auctionDao.uncancelPresent(auctionNo);
	}
	
	@GetMapping("/work/{auctionNo}")
	public AuctionLotDetailVO selectAuctionWithWork(@PathVariable int auctionNo) {
		return auctionDao.selectAuctionWithWork(auctionNo);
	}
	
}