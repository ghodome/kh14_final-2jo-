package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ArtistDto;
import com.art.vo.ArtistListRequestVO;
import com.art.vo.ArtistListVO;

@Repository
public class ArtistDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	//목록
	public List<ArtistDto> selectList(){
		return sqlSession.selectList("artist.list");
	}
	public int sequence() {
		return sqlSession.selectOne("artist.sequence");
	}
	//등록
	public void regist(ArtistDto artistDto) {
		sqlSession.insert("artist.regist",artistDto);
	}
	//수정
	public boolean update(ArtistDto artistDto) {
		return sqlSession.update("artist.update",artistDto)>0;
	}
	//삭제
	public boolean delete(int artistNo) {
		return sqlSession.delete("artist.delete",artistNo)>0;
	}
	
	//이미지 찾기
	public Integer findImage(int artistNo) {
		return sqlSession.selectOne("artist.findImage", artistNo);
	}
	//여러 이미지 찾기
	public List<Integer> findImages(int artistNo){
		return sqlSession.selectList("artist.findImages", artistNo);
	}
	
	//이미지 삭제
	public boolean deleteImage(int artistNo) {
		return sqlSession.delete("artist.deleteImage", artistNo)>0;
	}
	
	//연결 기능
	public void connect(int artist, int attachment) {
		Map<String,Object> params = new HashMap<>();
		params.put("artist", artist);
		params.put("attachment", attachment);
		sqlSession.insert("artist.connect", params);
	}
	
	//목록 카운트
	public int countWithPaging(ArtistListRequestVO requestVO) {
		return sqlSession.selectOne("artist.count", requestVO);
	}
	
	// 목록 + 페이징 + 검색
	public List<ArtistListVO> selectListByPaging(ArtistListRequestVO requestVO){
		return sqlSession.selectList("artist.list", requestVO);
	}
	
	// \
	public ArtistListVO selectOne(int artistNo) {
		return sqlSession.selectOne("artist.selectOne", artistNo);
	}
	
	
	
}
