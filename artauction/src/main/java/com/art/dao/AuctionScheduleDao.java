package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionScheduleDto;
import com.art.vo.AuctionScheduleListRequestVO;
import com.art.vo.AuctionScheduleListVO;
import com.art.vo.WorkListVO;


@Repository
public class AuctionScheduleDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	//일정등록
	public int sequence() {
		return sqlSession.selectOne("auctionSchedule.sequence");
	}
	public void insert(AuctionScheduleListVO auctionScheduleListVO) {
		sqlSession.insert("auctionSchedule.insert", auctionScheduleListVO);
	}
	
	//일정목록
//	public List<AuctionScheduleDto> selectList(){
//		return sqlSession.selectList("auctionSchedule.list");
//	}
	
	//일정상세
	public AuctionScheduleListVO selectOne(int auctionScheduleNo) {
		return sqlSession.selectOne("auctionSchedule.detail", auctionScheduleNo);
	}
	
	//일정상세(이미지포함)
		public AuctionScheduleListVO selectOneImage(int auctionScheduleNo) {
			return sqlSession.selectOne("auctionSchedule.detailall", auctionScheduleNo);
	}
	
//	//일정수정
	public boolean update(AuctionScheduleListVO scheduleListVO) {
		return sqlSession.update("auctionSchedule.update", scheduleListVO) > 0;
	}
		
//	//일정수정(이미지포함)
//	public boolean updateAll(AuctionScheduleListVO auctionScheduleListVO) {
//		return sqlSession.update("auctionSchedule.updateall", auctionScheduleListVO) > 0;
//	}
//		
//	public void updateAttachment(int auctionScheduleNo, int attachmentNo) {
//		 Map<String, Object> params = new HashMap<>();
//		 params.put("auctionScheduleNo", auctionScheduleNo);
//		 params.put("attachment", attachmentNo);
//		 sqlSession.update("auctionSchedule.updateAttachment", params);
//	}

	
	//일정삭제
	public boolean delete(int auctionScheduleNo) {
		return sqlSession.delete("auctionSchedule.delete", auctionScheduleNo) > 0;
	}
	
	//이미지 찾기(1) - 첫번째 이미지만 가져오기
	public Integer findImage(int auctionScheduleNo) {
		return sqlSession.selectOne("auctionSchedule.findImage", auctionScheduleNo);
	}
	
	//여러이미지
	public List<Integer> findImages(int auctionScheduleNo) {	
		return sqlSession.selectList("auctionSchedule.findImages", auctionScheduleNo);
	}
	
	//이미지 삭제
	public boolean deleteImage(int auctionScheduleNo) {
		return sqlSession.delete("auctionSchedule.deleteImage", auctionScheduleNo) > 0;
	}
	
	//이미지 연결기능
	public void connect(int auctionScheduleNo, int attachmentNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("auction", auctionScheduleNo);
		params.put("attachment", attachmentNo);
		sqlSession.insert("auctionSchedule.connect", params);
	}

	//목록 카운트
	public int countWithPaging(AuctionScheduleListRequestVO listRequestVO) {
		return sqlSession.selectOne("auctionSchedule.count", listRequestVO);
	}
	
	// 목록 페이징+검색 
	public List<AuctionScheduleListVO> selectListByPaging(AuctionScheduleListRequestVO listRequestVO) {
		return sqlSession.selectList("auctionSchedule.list", listRequestVO);
	}

	
}