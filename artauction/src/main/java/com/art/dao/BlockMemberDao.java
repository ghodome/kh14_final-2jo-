package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.BlockMemberDto;
import com.art.vo.MemberBlockVO;

@Repository
public class BlockMemberDao {

	@Autowired
	private SqlSession sqlSession;
	
	public void insertBlockedMember(BlockMemberDto blockMemberDto) {
        sqlSession.insert("blockMember.insertBlockMember", blockMemberDto); // 차단 회원 정보 삽입
    }

    public void deleteBlockedMember(String blockMemberId) {
        sqlSession.delete("blockMember.deleteBlockMember", blockMemberId); // 차단 해제
    }

    public List<MemberBlockVO> findBlockedMembers() {
        return sqlSession.selectList("blockMember.findBlockedMembers"); // 차단된 회원 목록 조회
    }
}
