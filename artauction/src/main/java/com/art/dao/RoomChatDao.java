package com.art.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.RoomChatDto;

@Repository
public class RoomChatDao {

    @Autowired
    private SqlSession sqlSession;

    // 목록 조회
    public List<RoomChatDto> selectList() {
        return sqlSession.selectList("roomchat.list");
    }

    // 회원별 목록 조회
    public List<RoomChatDto> selectListByMemberId(@Param("memberId") String memberId) {
        return sqlSession.selectList("roomchat.selectListByMemberId", memberId);
    }

    // 방 생성
    public void insert(RoomChatDto roomChatDto) {
        sqlSession.insert("roomchat.insert", roomChatDto);
    }
}
