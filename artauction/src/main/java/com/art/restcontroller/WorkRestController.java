package com.art.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.AttachmentDao;
import com.art.dao.workDao;
import com.art.dto.WorkDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.vo.WorkArtistVO;

@CrossOrigin
@RestController
@RequestMapping("/work")
public class WorkRestController {
	
	@Autowired
	private workDao workDao;
	@Autowired
	private AttachmentService attachmentService;
	
	@PostMapping(value="/",consumes = "multipart/form-data")
	public void insert(@RequestPart("workArtistVO") WorkArtistVO workArtistVO,
            @RequestPart("attach") MultipartFile attach) throws IllegalStateException, IOException {
		int workNo = workDao.sequence();
		workArtistVO.setWorkNo(workNo);
		workDao.insert(workArtistVO);
		if(!attach.isEmpty()) {
//			int attachmentNo = attachmentService.save(attach);
//			workDao.connect(workNo, attachmentNo);
		}
	}
	
	@GetMapping("/")
	public List<WorkArtistVO> list() {
		return workDao.selectList();
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
