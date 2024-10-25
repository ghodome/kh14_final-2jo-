package com.art.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AttachmentDao;
import com.art.dao.AuctionScheduleDao;
import com.art.dto.AuctionScheduleDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.service.TokenService;
import com.art.vo.AuctionScheduleListRequestVO;
import com.art.vo.AuctionScheduleListResponseVO;
//import com.art.vo.MemberClaimVO;
import com.art.vo.AuctionScheduleRequestVO;

@CrossOrigin
@RestController
@RequestMapping("/auctionSchedule")
public class AuctionScheduleRestController {
	
	@Autowired
	private AuctionScheduleDao auctionScheduleDao;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	
	//등록
	@Transactional
	@PostMapping(value="/", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void insert(
//				@RequestHeader("Authorization") String token,
				@ModelAttribute AuctionScheduleRequestVO requestVO) throws IllegalStateException, IOException {
		
//		System.out.println("Authorization Token: " + token);
		
		int auctionScheduleNo=auctionScheduleDao.sequence();
		
		AuctionScheduleDto auctionScheduleDto = new AuctionScheduleDto();
		auctionScheduleDto.setAuctionScheduleNo(auctionScheduleNo);
		auctionScheduleDto.setAuctionScheduleTitle(requestVO.getAuctionScheduleTitle());
		auctionScheduleDto.setAuctionScheduleStartDate(requestVO.getAuctionScheduleStartDate());
		auctionScheduleDto.setAuctionScheduleEndDate(requestVO.getAuctionScheduleEndDate());
		auctionScheduleDto.setAuctionScheduleState(requestVO.getAuctionScheduleState());
		auctionScheduleDto.setAuctionScheduleNotice(requestVO.getAuctionScheduleNotice());
		//DB저장
		auctionScheduleDao.insert(auctionScheduleDto);
		
		
	        for (MultipartFile attach : requestVO.getAttachList()) {
	        	if(attach.isEmpty()) continue;
	            int attachmentNo = attachmentService.save(attach);	//파일저장
	            // [4] 경매 일정과 첨부파일 연결
	            auctionScheduleDao.connect(auctionScheduleNo, attachmentNo);
	        }    
	}
	
	
	// 목록 조회 (이미지 포함)
	@PostMapping("/")
	public AuctionScheduleListResponseVO list(@RequestBody AuctionScheduleListRequestVO listRequestVO) {
		//페이징 정보 정리
		int count = auctionScheduleDao.countWithPaging(listRequestVO);
		boolean last = listRequestVO.getEndRow() == null || count <= listRequestVO.getEndRow(); 
		
		AuctionScheduleListResponseVO listResponseVO = new AuctionScheduleListResponseVO();
		listResponseVO.setAuctionScheduleList(auctionScheduleDao.selectListByPaging(listRequestVO));
		listResponseVO.setCount(count);
		listResponseVO.setLast(last);
		
		return listResponseVO;
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
