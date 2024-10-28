package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.WorkDto;
import com.art.vo.WorkArtistVO;
import com.art.vo.WorkListRequestVO;
import com.art.vo.WorkListVO;

@Repository
public class workDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("work.sequence");
	}
	//목록
	public List<WorkArtistVO> selectList() {
		return sqlSession.selectList("work.list");
	}
	//등록
	public void insert(WorkDto workDto) {
		sqlSession.insert("work.insert",workDto);
	}
	//삭제
	public boolean delete(int workNo) {
		return sqlSession.delete("work.delete", workNo)>0;
	}
	//수정
	public boolean update(WorkArtistVO workArtistVO) {
		return sqlSession.update("work.update", workArtistVO) > 0;
	}
	
	// 이미지 찾기 - 여러 이미지 중 첫 번째만 가져오기
	public Integer findImage(int workNo) {
		return sqlSession.selectOne("work.findImage", workNo);
	}
	
	// 이미지 찾기 - 여러 이미지를 가져오기
	public List<Integer> findImages(int workNo) {
		return sqlSession.selectList("work.findImages", workNo);
	}
	
	//이미지 지우기
	public boolean deleteImage(int workNo) {
		return sqlSession.delete("work.deleteImage", workNo) > 0;
	}

	
	//연결기능
	public void connect(int work, int attachment)  {
        Map<String, Object> params = new HashMap<>();
        params.put("work", work);
        params.put("attachment", attachment);
        sqlSession.insert("work.connect", params);
	}

	//목록 카운트
	public int countWithPaging(WorkListRequestVO requestVO) {
		return sqlSession.selectOne("work.count", requestVO);
	}
	
	// 목록 + 페이징 + 검색
	public List<WorkListVO> selectListByPaging(WorkListRequestVO requestVO){
		return sqlSession.selectList("work.list", requestVO);
	}
	
	// \
	public WorkListVO selectOne(int workNo) {
		return sqlSession.selectOne("work.selectOne", workNo);
	}

	
}
