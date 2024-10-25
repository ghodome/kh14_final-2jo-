package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.ChatDao;
import com.art.dto.ChatDto;


@CrossOrigin
@RestController
@RequestMapping("/chat")
public class ChatRestController {
	
	@Autowired
	private ChatDao chatDao;

	
	 //목록
  	@GetMapping("/list")
  	public List<ChatDto> list() {
  		return chatDao.selectList();
  	}
  	
  	//보내기
  	@PostMapping("/send")
  	public void insert(@RequestBody ChatDto chatDto) {
  		chatDao.insert(chatDto);
  	}
}
