package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.workDao;
import com.art.dto.WorkDto;
import com.art.vo.WorkArtistVO;

@CrossOrigin
@RestController
@RequestMapping("/work")
public class WorkRestController {
	
	@Autowired
	private workDao workDao;
	
	@GetMapping("/")
	public List<WorkArtistVO> list() {
		return workDao.selectList();
	}
	
	@PostMapping("/")
	public void insert(@RequestBody WorkArtistVO workArtistVO) {
		workDao.insert(workArtistVO);
	}
}
