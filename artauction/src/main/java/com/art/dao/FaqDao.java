package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.FaqDto;


@Repository
public class FaqDao {

	@Autowired
	private SqlSession sqlSession;
	
	//목록
	public List<FaqDto> selectList() {
		return sqlSession.selectList("faq.list");
	}

	//등록
	public void insert(FaqDto faqDto) {
		sqlSession.insert("faq.registration", faqDto);
	}

	//삭제
	public boolean delete(int faqNo) {
		int result = sqlSession.delete("faq.remove", faqNo);
		return result > 0;
		}
		
	}
			
		
