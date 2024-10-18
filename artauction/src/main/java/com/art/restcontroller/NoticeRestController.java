package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.NoticeDao;
import com.art.dto.NoticeDto;



@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeRestController {
	
	@Autowired
	private NoticeDao noticeDao;
	
	//등록
	@PostMapping("/registration")
	public void insert(@RequestBody NoticeDto noticeDto) {
		noticeDao.insert(noticeDto);
	}
	//목록
	@GetMapping("/list")
	public List<NoticeDto> list() {
		return noticeDao.selectList();
	}

}
