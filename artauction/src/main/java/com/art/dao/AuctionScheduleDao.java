package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionScheduleDto;



@Repository
public class AuctionScheduleDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	//일정등록
	public int sequence() {
		return sqlSession.selectOne("auctionSchedule.sequence");
	}
	public void insert(AuctionScheduleDto auctionScheduleDto) {
		sqlSession.insert("auctionSchedule.insert", auctionScheduleDto);
	}
	
	//일정목록
	public List<AuctionScheduleDto> selectList(){
		return sqlSession.selectList("auctionSchedule.list");
	}
	
	//일정상세
	public AuctionScheduleDto selectOne(int auctionScheduleNo) {
		return sqlSession.selectOne("auctionSchedule.detail", auctionScheduleNo);
	}
	
	//일정수정
	public boolean update(AuctionScheduleDto auctionScheduleDto) {
		return sqlSession.update("auctionSchedule.update", auctionScheduleDto) > 0;
	}
	
	//일정삭제
	public boolean delete(int auctionScheduleNo) {
		return sqlSession.delete("auctionSchedule.delete", auctionScheduleNo) > 0;
	}
	
	
	//이미지 연결기능
	public void connect(int auctionScheduleNo, int attachmentNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("auctionScheduleNo", auctionScheduleNo);
		params.put("attachmentNo", attachmentNo);
		sqlSession.insert("auctionSchedule.connect", params);
	}

	
	
	
}
