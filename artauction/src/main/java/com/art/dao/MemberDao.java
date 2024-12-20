package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.MemberDto;
import com.art.vo.MemberComplexRequestVO;
import com.art.vo.MemberInventoryVO;

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
	public List<MemberDto> search(MemberComplexRequestVO vo){
		return sqlSession.selectList("member.search", vo);
	}
	public int searchCount(MemberComplexRequestVO vo) {
		return sqlSession.selectOne("member.searchCount", vo);
	}
	public MemberDto selectOne(String memberId) {
		return sqlSession.selectOne("member.find", memberId);
	}
	public boolean pointUpdate(MemberDto dto) {
		return sqlSession.update("member.pointUpdate",dto)>0;
	}

	public List<MemberInventoryVO> selectMemberInventory(String memberId) {
		return sqlSession.selectList("member.findWithInventory",memberId);
	}

	public boolean isBlocked(String memberId) {
	    return sqlSession.selectOne("blockMember.isBlocked", memberId);
	}

	public boolean isIdAvailable(String memberId) {
	    Integer count = sqlSession.selectOne("member.isIdAvailable", memberId);
	    return count != null && count == 0; 
	}

	public boolean isNameAvailable(String memberName) {
	    Integer count = sqlSession.selectOne("member.isNameAvailable", memberName);
	    return count != null && count == 0;
	}
	public long selectPoint(String memberId) {
		return sqlSession.selectOne("member.selectPoint",memberId);
		
	}
	public void reducePoint(int point,String memberId) {
		Map data=Map.of("point",point,"memberId",memberId);
		sqlSession.update("member.reducePoint",data);
	}
	public void refundPoint(int point, String memberId) {
		Map data=Map.of("point",point,"memberId",memberId);
		sqlSession.update("member.refundPoint",data);
	}



}
