package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.RoomMessageDto;
import com.art.vo.WebsocketMessageVO;

@Repository
public class RoomMessageDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("roomMessage.sequence");
	}
	
	public void insert(RoomMessageDto roomMessageDto) {
		sqlSession.insert("roomMessage.add", roomMessageDto);
	}
	
	// 1:1 채팅 메시지 조회
	public List<WebsocketMessageVO> selectListDirectMessage(String senderId, String receiverId) {
		Map<String, Object> params = new HashMap<>();
		params.put("senderId", senderId);
		params.put("receiverId", receiverId);
		return sqlSession.selectList("roomMessage.listDirectMessage", params);
	}
}