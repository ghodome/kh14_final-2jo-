package com.art.restcontroller;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AuctionDao;
import com.art.dto.AuctionDataCollectionDto;
import com.art.dto.AuctionDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.vo.AuctionDataCollectionListRequestVO;
import com.art.vo.AuctionDataCollectionListResponseVO;
import com.art.vo.AuctionDataCollectionListVO;
import com.art.vo.AuctionLotDetailVO;
import com.art.vo.AuctionRequestVO;

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
	
	@Transactional
	@PostMapping(value="/", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void add(@ModelAttribute AuctionRequestVO requestVO) throws IllegalStateException, IOException {
		int auctionNo = auctionDao.sequence();
		
		AuctionDto auctionDto = new AuctionDto();
		auctionDto.setAuctionNo(auctionNo);
		auctionDto.setAuctionScheduleNo(requestVO.getAuctionScheduleNo());
		auctionDto.setWorkNo(requestVO.getWorkNo());
		auctionDto.setAuctionLot(requestVO.getAuctionLot());
		int startPrice = requestVO.getAuctionStartPrice();
		auctionDto.setAuctionStartPrice(startPrice);
		auctionDto.setAuctionLowPrice(requestVO.getAuctionLowPrice());
		auctionDto.setAuctionHighPrice(requestVO.getAuctionHighPrice());
		auctionDto.setAuctionState(requestVO.getAuctionState());
		auctionDto.setAuctionConsigner(requestVO.getAuctionConsigner());
		auctionDto.setAuctionConsignmentFee(requestVO.getAuctionConsignmentFee());
		auctionDto.setAuctionNetProceeds(requestVO.getAuctionNetProceeds());
		
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
		
//		종료 시간을 (lot/10)*5분씩 더해주기
		Calendar cal = Calendar.getInstance();
		log.info("auctionDto ={}", auctionDto);
		auctionDao.insert(auctionDto);
		
		for (MultipartFile attach : requestVO.getAttachList()) {
        	if(attach.isEmpty()) continue;
            int attachmentNo = attachmentService.save(attach);	//파일저장
            // [4] 경매 일정과 첨부파일 연결
            auctionDao.connect(auctionNo, attachmentNo);
        } 
	}
	
	@PatchMapping("/")
	public void update(@RequestBody AuctionDto auctionDto) {
		auctionDao.update(auctionDto);
	}
	
	//출품작 상세(이미지포함)
	@GetMapping("/detail/{auctionNo}")
	public AuctionDataCollectionListVO detail(@PathVariable int auctionNo) {
		AuctionDataCollectionListVO collectionListVO = auctionDao.selectOneImage(auctionNo);
		if(collectionListVO == null) {
			throw new TargetNotFoundException();
		}
		return collectionListVO;
	}
	
	//출품작 삭제(이미지포함)
	@DeleteMapping("/{auctionNo}")
	public void delete(@PathVariable int auctionNo) {
		AuctionDataCollectionListVO collectionListVO =  auctionDao.selectOneImage(auctionNo);
		
		if(collectionListVO == null) {	//삭제하지 못했다면 200이 아니라 404로 처리
			throw new TargetNotFoundException();
		}
		
		List<Integer> list = auctionDao.findImages(auctionNo);
		for(int i=0; i<list.size(); i++) {
			attachmentService.delete(list.get(i));			
		}
		
		auctionDao.delete(auctionNo);
		auctionDao.deleteImage(auctionNo);
	}
	
	//출품목록조회(이미지포함)
	@PostMapping("/{auctionScheduleNo}")
	public AuctionDataCollectionListResponseVO collectionList(
			@RequestBody AuctionDataCollectionListRequestVO listRequestVO) {
		//페이징 정보 정리
		int count = auctionDao.countWithPaging(listRequestVO);
		boolean last = listRequestVO.getEndRow() == null || count <= listRequestVO.getEndRow();
	
		Integer beginRow = listRequestVO.getBeginRow() != null ?  listRequestVO.getBeginRow() : 1;
		Integer endRow = listRequestVO.getEndRow() != null ?  listRequestVO.getEndRow() : count;
		listRequestVO.setBeginRow(beginRow);
		listRequestVO.setEndRow(endRow);
		
		AuctionDataCollectionListResponseVO listResponseVO = new AuctionDataCollectionListResponseVO();
		listResponseVO.setCollectionList(auctionDao.selectListByPaging(listRequestVO));
		listResponseVO.setCount(count);
		listResponseVO.setLast(last);
		
		System.out.println("begin = " + beginRow);
		System.out.println("end = " + endRow);
		return listResponseVO;
	}
	
	@GetMapping("/auctionList/{auctionScheduleNo}")
	public List<AuctionDataCollectionDto> auctionListWithJoin(@PathVariable int auctionScheduleNo){
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
	@GetMapping("/work/{auctionNo}")
	public AuctionLotDetailVO selectAuctionWithWork(@PathVariable int auctionNo) {
		return auctionDao.selectAuctionWithWork(auctionNo);
	}
	
}
