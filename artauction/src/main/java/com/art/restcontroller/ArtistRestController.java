package com.art.restcontroller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.art.dao.ArtistDao;
import com.art.dto.ArtistDto;
import com.art.error.TargetNotFoundException;
import com.art.service.AttachmentService;
import com.art.vo.ArtistEditRequestVO;
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
	
//	@PatchMapping("/")
//	public String update(@RequestBody ArtistDto artistDto) {
//		return artistDao.update(artistDto)?"성공":"실패";
//		
//	}
	
	@Transactional
	@PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void update(@ModelAttribute ArtistEditRequestVO requestVO) throws IllegalStateException, IOException {
		ArtistListVO originDto = artistDao.selectOne(requestVO.getArtistNo());
		if(originDto == null) throw new TargetNotFoundException("존재하지 않는 작가 번호입니다.");
		//수정전
		Set<Integer> before = new HashSet<>();
		List<Integer> beforeList = artistDao.findImages(originDto.getArtistNo());
		for(int i=0; i<beforeList.size(); i++) {
			before.add(beforeList.get(i));
		}
		//수정후
		//- originList : 기존 첨부 처리
		Set<Integer> after = new HashSet<>();
		artistDao.deleteImage(requestVO.getArtistNo());
		int afterSize = requestVO.getOriginList().size();
		for(int i=0; i<afterSize; i++) {
			int attachmentNo = requestVO.getOriginList().get(i);
			artistDao.connect(requestVO.getArtistNo(), attachmentNo);
			after.add(attachmentNo);
		}
		
		
		//수정전 - 수정후 계산 : 살려야 항 번호만 담긴 originList의 번호들과 겹치지 않는 번호들만 남김
		before.removeAll(after);
		
		//before에 남아있는 번호에 해당하는 파일을 모두 삭제 : 겹치짖 않는 번호들 삭제
		for(int attachmentNo : before) {
			attachmentService.delete(attachmentNo);// db+파일 삭제
		}
		
		//- attachList : 신규 첨부
		if( requestVO.getAttachList() != null ) {
			int attachListSize = requestVO.getAttachList().size();
			for(int i=0; i<attachListSize; i++) {
				int attachmentNo = attachmentService.save(requestVO.getAttachList().get(i));
				artistDao.connect(requestVO.getArtistNo(), attachmentNo);
				after.add(attachmentNo); // 저장소에 추가
			}
		}
		ArtistListVO artistListVO = new ArtistListVO();
		artistListVO.setArtistNo(requestVO.getArtistNo());
		artistListVO.setArtistName(requestVO.getArtistName());
		artistListVO.setArtistDescription(requestVO.getArtistDescription());
		artistListVO.setArtistHistory(requestVO.getArtistHistory());
		artistListVO.setArtistBirth(requestVO.getArtistBirth());
		artistListVO.setArtistDeath(requestVO.getArtistDeath());
		
		artistDao.update(artistListVO);
	}
	
}




























