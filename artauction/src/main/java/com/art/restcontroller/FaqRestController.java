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

import com.art.dao.FaqDao;
import com.art.dto.FaqDto;
import com.art.error.TargetNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/faq")
public class FaqRestController {

	@Autowired
	private FaqDao faqDao;

	// 목록
	@GetMapping("/list")
	public List<FaqDto> list() {
		return faqDao.selectList();
	}

	// 등록
	@PostMapping("/plus")
	public void insert(@RequestBody FaqDto faqDto) {
		int faqNo = faqDao.sequence();
		faqDto.setFaqNo(faqNo);
		faqDao.insert(faqDto);
	}

	// 삭제
	@DeleteMapping("/{faqNo}")
	public void delete(@PathVariable int faqNo) {
		faqDao.delete(faqNo);
	}

	// 검색
	@GetMapping("/column/{column}/keyword/{keyword}")
	public List<FaqDto> search(@PathVariable String column, @PathVariable String keyword) {
		return faqDao.selectList(column, keyword);
	}
	// 수정
		@PutMapping("/")
		public void update(@RequestBody FaqDto faqDto) {
			boolean result = faqDao.update(faqDto);
			if(result == false) throw new TargetNotFoundException();
		}
}
