package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.NoticeDto;


@Repository
public class NoticeDao {

	@Autowired
	private SqlSession sqlSession;
	
	//등록
	public void insert(NoticeDto noticeDto) {
		sqlSession.insert("notice.registration", noticeDto);
	}
	//목록
	public List<NoticeDto> selectList() {
		return sqlSession.selectList("notice.list");
	}
	//상세
	public NoticeDto selectOne(int noticeNo) {
		return sqlSession.selectOne("notice.detail", noticeNo);
	}
}
