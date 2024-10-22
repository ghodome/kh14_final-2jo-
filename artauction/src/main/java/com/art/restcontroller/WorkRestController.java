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

import com.art.dao.workDao;
import com.art.dto.WorkDto;
import com.art.error.TargetNotFoundException;
import com.art.vo.WorkArtistVO;

@CrossOrigin
@RestController
@RequestMapping("/work")
public class WorkRestController {
	
	@Autowired
	private workDao workDao;
	
	@PostMapping("/")
	public void insert(@RequestBody WorkArtistVO workArtistVO) {
		workDao.insert(workArtistVO);
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
