package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.RoomDto;
import com.art.dto.RoomMemberDto;
import com.art.vo.RoomVO;

@Repository
public class RoomDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("room.sequence");
	}
	public void insert(RoomDto roomDto) {
		sqlSession.insert("room.insert", roomDto);
	}
	
	public List<RoomDto> selectList() {
		return sqlSession.selectList("room.list");
	}
	public List<RoomVO> selectList(String memberId) {
		return sqlSession.selectList("room.listByMember", memberId);
	}
	
	public RoomDto selectOne(int roomNo) {
		return sqlSession.selectOne("room.find", roomNo);
	}
	
	public boolean update(RoomDto roomDto) {
		return sqlSession.update("room.edit", roomDto) > 0;
	}
	
	public boolean delete(int roomNo) {
		return sqlSession.delete("room.delete", roomNo) > 0;
	}
	
	//채팅방 입장
	public void enter(RoomMemberDto roomMemberDto) {
		sqlSession.insert("roomMember.enter", roomMemberDto);
	}
	//채팅방 퇴장
	public boolean leave(RoomMemberDto roomMemberDto) {
		return sqlSession.delete("roomMember.leave", roomMemberDto) > 0;
	}
	
	//채팅방 자격 검사
	public boolean check(RoomMemberDto roomMemberDto) {
		int result = sqlSession.selectOne("roomMember.check", roomMemberDto);
		return result > 0;
	}

	
}