package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.ArtistDao;
import com.art.dto.ArtistDto;

@RestController
@CrossOrigin(origins={"http://localhost:3000"})
@RequestMapping("/artist")
public class ArtistRestController {
	
	@Autowired
	private ArtistDao artistDao;
	
	@GetMapping("/")
	public List<ArtistDto> list(){
		return artistDao.selectList();
	}
}
