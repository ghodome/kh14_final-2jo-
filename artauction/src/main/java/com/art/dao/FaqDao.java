package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.FaqDto;
import com.art.dto.NoticeDto;

@Repository
public class FaqDao {

	@Autowired
	private SqlSession sqlSession;

	// 목록
	public List<FaqDto> selectList() {
		return sqlSession.selectList("faq.list");
	}

	// 시퀀스 생성
	public int sequence() {
		return sqlSession.selectOne("faq.sequence");
	}

	// 등록
	public void insert(FaqDto faqDto) {
		sqlSession.insert("faq.registration", faqDto);
	}

	// 삭제
	public boolean delete(int faqNo) {
		int result = sqlSession.delete("faq.remove", faqNo);
		return result > 0;
	}

	// 검색
	public List<FaqDto> selectList(String column, String keyword) {
		Map<String, Object> params = new HashMap<>();
		params.put("column", column);
		params.put("keyword", keyword);
		return sqlSession.selectList("faq.search", params);
	}

	// 수정
	public boolean update(FaqDto faqDto) {
		int result = sqlSession.update("faq.update", faqDto);
		return result > 0;
	}

}
