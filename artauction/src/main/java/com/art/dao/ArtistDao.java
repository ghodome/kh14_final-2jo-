package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ArtistDto;

@Repository
public class ArtistDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<ArtistDto> selectList(){
		return sqlSession.selectList("artist.selectList");
				
	}
}
