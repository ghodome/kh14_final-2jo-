package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ChatDto;
import com.art.vo.WebsocketMessageVO;

@Repository
public class ChatDao {
    @Autowired
    private SqlSession sqlSession;

    // 메시지 시퀀스 번호 생성
    public int sequence() {
        return sqlSession.selectOne("chat.sequence"); // 매핑 XML에서 chat로 수정
    }

    // 메시지 추가 (1:1 채팅 메시지)
    public void insert(ChatDto chatDto) { // ChatDto 사용
        sqlSession.insert("chat.add", chatDto); // 매핑 XML에서 chat로 수정
    }

    // 특정 회원의 메시지 목록 조회 (1:1 채팅용)
    public List<WebsocketMessageVO> selectListMember(String memberId, int beginRow, int endRow) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("beginRow", beginRow);
        params.put("endRow", endRow);
        return sqlSession.selectList("chat.listMember", params); // 매핑 XML에서 chat로 수정
    }
}