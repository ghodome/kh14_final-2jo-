package com.art.restcontroller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.NoticeDao;
import com.art.dto.NoticeDto;
import com.art.error.TargetNotFoundException;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeRestController {

	@Autowired
	private NoticeDao noticeDao;
	
//	@Autowired
//	private TokenService tokenService;

	// 등록
	@PostMapping("/plus")
	   public void insert(@RequestBody NoticeDto noticeDto) {
//			   @RequestHeader("Authorization") String token) {
//		   System.out.println("Received token: " + token);
//			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		int noticeNo = noticeDao.sequence();
		noticeDto.setNoticeNo(noticeNo);
//		noticeDto.setNoticeMemberId(claimVO.getMemberId());
//		noticeDto.getNoticeType();
//		noticeDto.getNoticeTitle();
//		noticeDto.getNoticeContent();
//		noticeDto.getNoticeWtime();	
        noticeDao.insert(noticeDto);
    }
	
	// 목록
	@GetMapping("/list")
	public List<NoticeDto> list() {
		return noticeDao.selectList();
	}

	// 상세
	@GetMapping("/{noticeNo}")
	public NoticeDto detail(@PathVariable int noticeNo) {
		return noticeDao.selectOne(noticeNo);

//		NoticeDto noticeDto = noticeDao.selectOne(noticeNo);
//		if(noticeDto == null) throw new TargetNotFoundException();
//		return noticeDto;//나중에 바꿀꺼
	}

//	검색
	@GetMapping("/column/{column}/keyword/{keyword}")
	public List<NoticeDto> search(@PathVariable String column, @PathVariable String keyword) {
		return noticeDao.selectList(column, keyword);
	}

	// 삭제
	@DeleteMapping("/{noticeNo}")
	public void delete(@PathVariable int noticeNo) {
		noticeDao.delete(noticeNo);
	}
	// 수정
	@PutMapping("/")
	public void update(@RequestBody NoticeDto noticeDto) {
		boolean result = noticeDao.update(noticeDto);
		if(result == false) throw new TargetNotFoundException();
	}
	
}
