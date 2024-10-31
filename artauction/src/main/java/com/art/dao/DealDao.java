package com.art.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.DealDto;

@Repository
public class DealDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void insert(DealDto dealDto) {
		sqlSession.insert("deal.insert",dealDto);
	}
	
}
