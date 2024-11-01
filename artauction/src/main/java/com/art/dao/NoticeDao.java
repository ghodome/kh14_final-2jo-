package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.NoticeDto;


@Repository
public class NoticeDao {

	@Autowired
	private SqlSession sqlSession;
	
	//시퀀스 생성
	public int sequence() {
		return sqlSession.selectOne("notice.sequence");
	}
    // 등록
    public void insert(NoticeDto noticeDto) {
        sqlSession.insert("notice.registration", noticeDto);
    }
    
	//목록
	public List<NoticeDto> selectList() {
		return sqlSession.selectList("notice.list");
	}
	//상세
	public NoticeDto selectOne(int noticeNo) {
		return sqlSession.selectOne("notice.detail", noticeNo);
	}
	
	//검색
	public List<NoticeDto> selectList(String column, String keyword) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("column", column); 
	    params.put("keyword", keyword);
	    return sqlSession.selectList("notice.search", params);
	}
	//삭제
	public boolean delete(int noticeNo) {
		int result = sqlSession.delete("notice.remove", noticeNo);
		return result > 0;
	}
	
	//수정
	public boolean update(NoticeDto noticeDto) {
		int result = sqlSession.update("notice.update", noticeDto);
		return result>0;
	}

}
