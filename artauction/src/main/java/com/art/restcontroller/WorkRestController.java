package com.art.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AttachmentDao;
import com.art.dao.workDao;
import com.art.dto.WorkDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.service.TokenService;
import com.art.vo.WorkArtistVO;
import com.art.vo.WorkInsertRequestVO;
import com.art.vo.WorkListRequestVO;
import com.art.vo.WorkListResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/work")
public class WorkRestController {
	
	@Autowired
	private workDao workDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	
//	@PostMapping("/")
//	public void insert(@RequestBody WorkArtistVO workArtistVO) {
//		workDao.insert(workArtistVO);
//	}
	
	@Transactional
	@PostMapping(value="/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void insert(@RequestHeader ("Authorization") String token,
			@ModelAttribute WorkInsertRequestVO workInsertRequestVO) throws IllegalStateException, IOException {
		

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
	
//	@GetMapping("/")
//	public List<WorkArtistVO> list() {
//		return workDao.selectList();
//	}
	
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
		workDao.delete(workNo);
	}

	@PatchMapping("/")
	public void update(@RequestBody WorkArtistVO workArtistVO) {
		boolean result = workDao.update(workArtistVO);
		if(result == false) {
			throw new TargetNotFoundException();
		} 
	}
	
	
}
