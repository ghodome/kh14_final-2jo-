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
	public int sequence() {
		return sqlSession.selectOne("artist.sequence");
	}
	public void regist(ArtistDto artistDto) {
		sqlSession.insert("artist.regist",artistDto);
	}
	public boolean update(ArtistDto artistDto) {
		return sqlSession.update("artist.update",artistDto)>0;
	}
	public boolean delete(int artistNo) {
		return sqlSession.delete("artist.delete",artistNo)>0;
	}
}
