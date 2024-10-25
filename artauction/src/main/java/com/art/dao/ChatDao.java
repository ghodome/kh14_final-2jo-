package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ChatDto;

@Repository
public class ChatDao {

	@Autowired
	private SqlSession sqlSession;

	// 목록 조회
	public List<ChatDto> selectList() {
		return sqlSession.selectList("chat.list");
	}

	// 보내기
	public void insert(ChatDto chatDto) {
		sqlSession.insert("chat.send", chatDto);
	}

}
