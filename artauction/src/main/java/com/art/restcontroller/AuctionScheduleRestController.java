package com.art.restcontroller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AuctionScheduleDao;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.vo.AuctionScheduleEditRequestVO;
//import com.art.service.TokenService;
import com.art.vo.AuctionScheduleListRequestVO;
import com.art.vo.AuctionScheduleListResponseVO;
import com.art.vo.AuctionScheduleListVO;
import com.art.vo.AuctionScheduleRequestVO;
import com.art.vo.WorkListVO;


@CrossOrigin
@RestController
@RequestMapping("/auctionSchedule")
public class AuctionScheduleRestController {
	
	@Autowired
	private AuctionScheduleDao auctionScheduleDao;
	
//	@Autowired
//	private TokenService tokenService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	//등록
	@Transactional
	@PostMapping(value="/", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public void insert(
			@ModelAttribute AuctionScheduleRequestVO requestVO) throws IllegalStateException, IOException {
		
		int auctionScheduleNo=auctionScheduleDao.sequence();
		
		AuctionScheduleListVO auctionScheduleListVO = new AuctionScheduleListVO();
		auctionScheduleListVO.setAuctionScheduleNo(auctionScheduleNo);
		auctionScheduleListVO.setAuctionScheduleTitle(requestVO.getAuctionScheduleTitle());
		auctionScheduleListVO.setAuctionScheduleStartDate(requestVO.getAuctionScheduleStartDate());
		auctionScheduleListVO.setAuctionScheduleEndDate(requestVO.getAuctionScheduleEndDate());
		auctionScheduleListVO.setAuctionScheduleState(requestVO.getAuctionScheduleState());
		auctionScheduleListVO.setAuctionScheduleNotice(requestVO.getAuctionScheduleNotice());
		//DB저장
		auctionScheduleDao.insert(auctionScheduleListVO);
		
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
		
		Integer beginRow = listRequestVO.getBeginRow() != null ? listRequestVO.getBeginRow() : 1;
		Integer endRow = listRequestVO.getEndRow() != null ?  listRequestVO.getEndRow() : count;
		listRequestVO.setBeginRow(beginRow);
	    listRequestVO.setEndRow(endRow);
	    
		
		AuctionScheduleListResponseVO listResponseVO = new AuctionScheduleListResponseVO();
		listResponseVO.setAuctionScheduleList(auctionScheduleDao.selectListByPaging(listRequestVO));
		listResponseVO.setCount(count);
		listResponseVO.setLast(last);

		return listResponseVO;
	}
	
	//상세 - 이미지 포함
	@GetMapping("/{auctionScheduleNo}")
	public AuctionScheduleListVO detailall(@PathVariable int auctionScheduleNo) {
		AuctionScheduleListVO auctionScheduleListVO = auctionScheduleDao.selectOneImage(auctionScheduleNo);
		if(auctionScheduleListVO == null) {
			throw new TargetNotFoundException();
		}
		return auctionScheduleListVO;
	}
	
	//수정 이미지 포함
	@Transactional
	@PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void update(@ModelAttribute AuctionScheduleEditRequestVO editRequestVO) throws IllegalStateException, IOException {
		AuctionScheduleListVO originDto = auctionScheduleDao.selectOne(editRequestVO.getAuctionScheduleNo());
		if(originDto == null) throw new TargetNotFoundException("존재하지 않는 경매 일정");
		
		//수정전
		Set<Integer> before =new HashSet<>();
		List<Integer> beforeList = auctionScheduleDao.findImages(originDto.getAuctionScheduleNo());
		for(int i=0; i<beforeList.size(); i++) {
			before.add(beforeList.get(i));//저장소 추가
		}
		
		//수정후
		//- originList : 기존 첨부 처리
		Set<Integer> after = new HashSet<>();
		auctionScheduleDao.deleteImage(editRequestVO.getAuctionScheduleNo());
		int afterSize = editRequestVO.getOriginList().size();
		for(int i=0; i<afterSize; i++) {
			int attachmentNo = editRequestVO.getOriginList().get(i);
			auctionScheduleDao.connect(editRequestVO.getAuctionScheduleNo(), attachmentNo);
			after.add(attachmentNo);//저장소 추가
		}
			
		// 수정전 - 수정후 계산 : 살려야할 번호만 담긴 originList의 번호들과 겹치지 않는 번호들만 남김
		before.removeAll(after);
		
		// before에 남아있는 번호에 해당하는 파일을 모두 삭제 : 겹치짖 않는 번호들 삭제
		for(int attachmentNo : before) {
			attachmentService.delete(attachmentNo); // db+파일 삭제
		}
		
		// - attachList : 신규 첨부
		if( editRequestVO.getAttachList() != null ) {
			int attachListSize = editRequestVO.getAttachList().size();
			for(int i=0; i<attachListSize; i++) {
				int attachmentNo = attachmentService.save(editRequestVO.getAttachList().get(i));
				auctionScheduleDao.connect(editRequestVO.getAuctionScheduleNo(), attachmentNo);
				after.add(attachmentNo); // 저장소에 추가
			}
		}
		//작품 정보처리
		AuctionScheduleListVO scheduleListVO = new AuctionScheduleListVO();
		scheduleListVO.setAuctionScheduleTitle(editRequestVO.getAuctionScheduleTitle());
		scheduleListVO.setAuctionScheduleStartDate(editRequestVO.getAuctionScheduleStartDate());
		scheduleListVO.setAuctionScheduleEndDate(editRequestVO.getAuctionScheduleEndDate());
		scheduleListVO.setAuctionScheduleState(editRequestVO.getAuctionScheduleState());
		scheduleListVO.setAuctionScheduleNotice(editRequestVO.getAuctionScheduleNotice());
		scheduleListVO.setAuctionScheduleNo(editRequestVO.getAuctionScheduleNo());
		 
		auctionScheduleDao.update(scheduleListVO);	
	}
	
	
	//삭제 (이미지포함 삭제)
	@DeleteMapping("/{auctionScheduleNo}")
	public void delete(@PathVariable int auctionScheduleNo) {
		AuctionScheduleListVO auctionScheduleListVO =  auctionScheduleDao.selectOne(auctionScheduleNo);
		
		if(auctionScheduleListVO == null) {	//삭제하지 못했다면 200이 아니라 404로 처리
			throw new TargetNotFoundException();
		}
		
		List<Integer> list = auctionScheduleDao.findImages(auctionScheduleNo);
		for(int i=0; i<list.size(); i++) {
			attachmentService.delete(list.get(i));			
		}
		
		auctionScheduleDao.delete(auctionScheduleNo);
		auctionScheduleDao.deleteImage(auctionScheduleNo);
	}
	

}