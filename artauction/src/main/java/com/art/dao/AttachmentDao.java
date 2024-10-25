package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AttachmentDto;
import com.art.dto.AuctionScheduleDto;

@Repository
public class AttachmentDao {
	
	@Autowired
	private SqlSession sqlSession;

	public int sequence() {
		return sqlSession.selectOne("attachment.sequence");
	}
	public void insert(AttachmentDto attachmentDto) {
		sqlSession.insert("attachment.insert", attachmentDto);
	}
	public boolean delete(int attachmentNo) {
		return sqlSession.delete("attachment.delete", attachmentNo)>0;
	}
//	public List<AttachmentDto> selectList(){
//		return sqlSession.selectList("attachment.list");
	public AttachmentDto selectOne(int attachmentNo) {
		return sqlSession.selectOne("attachment.detail", attachmentNo);
	}
	

	
}
