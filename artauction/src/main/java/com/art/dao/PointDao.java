package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.PointDto;

@Repository
public class PointDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(PointDto pointDto) {
		sqlSession.insert("point.insert",pointDto);
	}
	public List<PointDto> selectList(){
		return sqlSession.selectList("point.selectList");
	}
	public PointDto selectOne(int pointNo) {
		return sqlSession.selectOne("point.selectOne",pointNo);
	}
	public int sequence() {
		return sqlSession.selectOne("point.sequence");
	}
}
