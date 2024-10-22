package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.WorkDto;
import com.art.vo.WorkArtistVO;

@Repository
public class workDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("work.sequence");
	}
	
	public List<WorkArtistVO> selectList() {
		return sqlSession.selectList("work.list");
	}
	
	public void insert(WorkArtistVO workArtistVO) {
		
		sqlSession.insert("work.insert",workArtistVO);
	}
	
	public boolean delete(int workNo) {
		return sqlSession.delete("work.delete", workNo)>0;
	}
	
}
