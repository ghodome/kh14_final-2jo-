package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ChatDto;

@Repository
public class ChatDao {

	@Autowired
	private SqlSession sqlSession;

	// 시퀀스
	public int sequence() {
		return sqlSession.selectOne("chat.sequence");
	}

	// 목록 조회
	public List<ChatDto> selectList(int beginRow, int endRow) {
		Map<String, Object> params = Map.of("beginRow", beginRow, "endRow", endRow);
		return sqlSession.selectList("chat.list", params);
	}

	// 보내기
	public void insert(ChatDto chatDto) {
		sqlSession.insert("chat.send", chatDto);
	}

	
	
	
}
