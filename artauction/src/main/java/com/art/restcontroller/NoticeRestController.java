package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	//상세
	@GetMapping("/{noticeNo}")
	public NoticeDto detail(@PathVariable int noticeNo) {
		return noticeDao.selectOne(noticeNo);
		
//		NoticeDto noticeDto = noticeDao.selectOne(noticeNo);
//		if(noticeDto == null) throw new TargetNotFoundException();
//		return noticeDto;//나중에 바꿀꺼
	}
//	검색
	@GetMapping("/column/{column}/keyword/{keyword}")
	public List<NoticeDto> search(
			@PathVariable String column,
			@PathVariable String keyword
			){
		List<NoticeDto> list = noticeDao.selectList(column, keyword);
		return noticeDao.selectList(column, keyword);
	}

}
