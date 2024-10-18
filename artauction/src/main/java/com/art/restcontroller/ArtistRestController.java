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

import com.art.dao.ArtistDao;
import com.art.dto.ArtistDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	@PostMapping("/")
	public void regist(@RequestBody ArtistDto artistDto) {
		int artistNo=artistDao.sequence();
		artistDto.setArtistNo(artistNo);
		artistDao.regist(artistDto);
	}
	@PatchMapping("/")
	public String update(@RequestBody ArtistDto artistDto) {
		return artistDao.update(artistDto)?"성공":"실패";
		
	}
	@DeleteMapping("/{artistNo}")
	public void delete(@PathVariable int artistNo) {
		artistDao.delete(artistNo);
	}
}
