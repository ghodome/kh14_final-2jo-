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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AttachmentDao;
import com.art.dao.workDao;
import com.art.dto.WorkDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.service.TokenService;
import com.art.vo.WorkEditRequestVO;
import com.art.vo.WorkInsertRequestVO;
import com.art.vo.WorkListRequestVO;
import com.art.vo.WorkListResponseVO;
import com.art.vo.WorkListVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/work")
public class WorkRestController {
	
	@Autowired
	private workDao workDao;
	@Autowired
	private AttachmentService attachmentService; 
	@Autowired
	private AttachmentDao attachmentDao;
	
	
	@Transactional
	@PostMapping(value="/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void insert(@ModelAttribute WorkInsertRequestVO workInsertRequestVO) throws IllegalStateException, IOException {
		

	    // 작품 정보 생성
	    int workNo = workDao.sequence();
	    
	    WorkDto  workDto  = new WorkDto();
	    
	    workDto.setWorkNo(workNo);
	    workDto.setArtistNo(workInsertRequestVO.getArtistNo());
	    workDto.setWorkTitle(workInsertRequestVO.getWorkTitle());
	    workDto.setWorkDescription(workInsertRequestVO.getWorkDescription());
	    workDto.setWorkMaterials(workInsertRequestVO.getWorkMaterials());
	    workDto.setWorkSize(workInsertRequestVO.getWorkSize());
	    workDto.setWorkCategory(workInsertRequestVO.getWorkCategory());

	    // DB에 작품 정보 삽입
	    workDao.insert(workDto);

	    // 첨부 파일 처리
	    for (MultipartFile attach : workInsertRequestVO.getAttachList()) {
	        if (attach.isEmpty()) continue;
	        int attachmentNo = attachmentService.save(attach); // 파일 저장
	        workDao.connect(workNo, attachmentNo); // 작품과 첨부 파일 연결
	    }

	}
	
	@PostMapping("/")
	public WorkListResponseVO list (@RequestBody WorkListRequestVO requestVO) {
		// 페이징 정보 정리
		int count = workDao.countWithPaging(requestVO);
		boolean last = requestVO.getEndRow() == null || count <= requestVO.getEndRow();
		
		WorkListResponseVO responseVO = new WorkListResponseVO();
		
		responseVO.setWorkList(workDao.selectListByPaging(requestVO));
		responseVO.setCount(count);
		responseVO.setLast(last);
		
		return responseVO;
	}
	
	@DeleteMapping("/{workNo}")
		public void delete(@PathVariable int workNo) {
			WorkListVO workListVO = workDao.selectOne(workNo);
		if(workListVO == null) throw new TargetNotFoundException("존재하지 않는 상품 번호");
		
		List<Integer> list = workDao.findImages(workNo);
		for(int i=0; i<list.size(); i++) {
			System.out.println("지워지는번호 : "+list.get(i));
			attachmentService.delete(list.get(i));
		}
		
		workDao.delete(workNo); // 상품 정보 삭제
		workDao.deleteImage(workNo); // 연결 테이블 정보 삭제
	}
 
	//수정 이미지 포함
	@Transactional
	@PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void update(@ModelAttribute WorkEditRequestVO requestVO) throws IllegalStateException, IOException {
		WorkListVO originDto = workDao.selectOne(requestVO.getWorkNo());
		if(originDto == null) throw new TargetNotFoundException("존재하지 않는 상품 번호");
		
		//수정전
		Set<Integer> before =new HashSet<>();
		List<Integer> beforeList = workDao.findImages(originDto.getWorkNo());
		for(int i=0; i<beforeList.size(); i++) {
			before.add(beforeList.get(i));//저장소 추가
		}
		
		//수정후
		//- originList : 기존 첨부 처리
		Set<Integer> after = new HashSet<>();
		workDao.deleteImage(requestVO.getWorkNo());
		int afterSize = requestVO.getOriginList().size();
		for(int i=0; i<afterSize; i++) {
			int attachmentNo = requestVO.getOriginList().get(i);
			workDao.connect(requestVO.getWorkNo(), attachmentNo);
			after.add(attachmentNo);//저장소 추가
		}
		
		// 수정전 - 수정후 계산 : 살려야할 번호만 담긴 originList의 번호들과 겹치지 않는 번호들만 남김
		before.removeAll(after);
		
		// before에 남아있는 번호에 해당하는 파일을 모두 삭제 : 겹치짖 않는 번호들 삭제
		for(int attachmentNo : before) {
			attachmentService.delete(attachmentNo); // db+파일 삭제
		}
		
		// - attachList : 신규 첨부
		if( requestVO.getAttachList() != null ) {
			int attachListSize = requestVO.getAttachList().size();
			for(int i=0; i<attachListSize; i++) {
				int attachmentNo = attachmentService.save(requestVO.getAttachList().get(i));
				workDao.connect(requestVO.getWorkNo(), attachmentNo);
				after.add(attachmentNo); // 저장소에 추가
			}
		}
		//작품 정보처리
		WorkListVO workListVO = new WorkListVO();
		workListVO.setWorkTitle(requestVO.getWorkTitle());
		workListVO.setArtistNo(requestVO.getArtistNo());
		workListVO.setWorkDescription(requestVO.getWorkDescription());
		workListVO.setWorkMaterials(requestVO.getWorkMaterials());
		workListVO.setWorkSize(requestVO.getWorkSize());
		workListVO.setWorkCategory(requestVO.getWorkCategory());
		workListVO.setWorkNo(requestVO.getWorkNo());
		 
		workDao.update(workListVO);
		
	}

}