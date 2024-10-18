package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.WorkDto;
import com.art.vo.WorkaArtistVO;

@Repository
public class workDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<WorkDto> selectList() {
		return sqlSession.selectList("work.list");
	}
	
	public void insert(WorkaArtistVO workArtistVO) {
		
		sqlSession.insert("work.insert",workArtistVO);
	}
	public int sequence() {
		return sqlSession.selectOne("work.sequence");
	}
	
}
