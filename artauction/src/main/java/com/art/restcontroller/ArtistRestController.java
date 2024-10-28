package com.art.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.ArtistDao;
import com.art.dto.ArtistDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.vo.ArtistInsertRequestVO;
import com.art.vo.ArtistListRequestVO;
import com.art.vo.ArtistListResponseVO;
import com.art.vo.ArtistListVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins={"http://localhost:3000"})
@RequestMapping("/artist")
public class ArtistRestController {
	
	@Autowired
	private ArtistDao artistDao;
	@Autowired
	private AttachmentService attachmentService;
	
//	목록
//	@GetMapping("/")
//	public List<ArtistDto> list(){
//		return artistDao.selectList();
//	}
	
	@PostMapping("/")
	public ArtistListResponseVO list (@RequestBody ArtistListRequestVO requestVO) {
		//페이징 정보 정리
		int count = artistDao.countWithPaging(requestVO);
		boolean last = requestVO.getEndRow() == null || count <= requestVO.getEndRow();
		
		ArtistListResponseVO responseVO = new ArtistListResponseVO();
		
		responseVO.setArtistList(artistDao.selectListByPaging(requestVO));
		responseVO.setCount(count);
		responseVO.setLast(last);
		
		return responseVO;
	}
	
//	등록
@Transactional
@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public void regist(
//		@RequestHeader ("Authorization") String token,
		@ModelAttribute ArtistInsertRequestVO artistInsertRequestVO) throws IllegalStateException, IOException {
	
	//작가 정보 생성
	int artistNo=artistDao.sequence();
	ArtistDto artistDto = new ArtistDto();
	
	artistDto.setArtistNo(artistNo);
	artistDto.setArtistName(artistInsertRequestVO.getArtistName());
	artistDto.setArtistDescription(artistInsertRequestVO.getArtistDescription());
	artistDto.setArtistHistory(artistInsertRequestVO.getArtistHistory());
	artistDto.setArtistBirth(artistInsertRequestVO.getArtistBirth());
	artistDto.setArtistDeath(artistInsertRequestVO.getArtistDeath());
	
	//db에 작품 정보 삽입
	artistDao.regist(artistDto);
	
	//첨부파일 처리
	for(MultipartFile attach : artistInsertRequestVO.getAttachList()) {
		if(attach.isEmpty()) continue;
		int attachmentNo = attachmentService.save(attach);//파일저장
		artistDao.connect(artistNo, attachmentNo);//작가와 첨부 파일 연결
	}
	
}
	
//	삭제 - (이미지 포함)
	@DeleteMapping("/{artistNo}")
	public void delete(@PathVariable int artistNo) {
		ArtistListVO artistListVO = artistDao.selectOne(artistNo);
		if(artistListVO == null) throw new TargetNotFoundException("존재하지 않는 작가 번호입니다.");
		
		List<Integer> list = artistDao.findImages(artistNo);
		for(int i=0; i<list.size(); i++) {
			attachmentService.delete(list.get(i));
		}
		
		artistDao.delete(artistNo);
		artistDao.deleteImage(artistNo);
	}
	
	@PatchMapping("/")
	public String update(@RequestBody ArtistDto artistDto) {
		return artistDao.update(artistDto)?"성공":"실패";
		
	}
	
}
