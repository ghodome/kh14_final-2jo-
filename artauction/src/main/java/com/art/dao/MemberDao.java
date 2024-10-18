package com.art.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.MemberDto;

@Repository
public class MemberDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void insert(MemberDto dto) {
		sqlSession.insert("member.insert", dto);
	}
	public boolean update(MemberDto dto) {
		return sqlSession.update("member.update", dto) > 0;
	}
	public boolean delete(String memberId) {
		return sqlSession.delete("member.delete", memberId) > 0;
	}

	public MemberDto selectOne(String memberId) {
		return sqlSession.selectOne("member.find", memberId);
		
	}

}
