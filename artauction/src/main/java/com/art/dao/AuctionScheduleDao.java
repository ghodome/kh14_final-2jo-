package com.art.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionScheduleDto;
import com.art.vo.AuctionDataCollectionListVO;
import com.art.vo.AuctionScheduleListRequestVO;


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
	
	//일정수정
	public boolean update(AuctionScheduleDto auctionScheduleDto) {
		return sqlSession.update("auctionSchedule.update", auctionScheduleDto) > 0;
	}
	
	//일정수정(이미지포함)
		public boolean updateAll(AuctionScheduleListVO auctionScheduleListVO) {
			return sqlSession.update("auctionSchedule.updateall", auctionScheduleListVO) > 0;
		}
	
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
	//경매시작시간 해당 리스트 출력 메서드
	public List<Integer> selectListStarted(String startTime){
		Map model=Map.of("startTime",startTime);
		return sqlSession.selectList("auctionSchedule.listStarted",model);
	}
	//경매종료시간 해당 리스트 검사 메서드
	public List<Integer> selectListTerminated(String endTime){
		Map model=Map.of("endTime",endTime);
		return sqlSession.selectList("auctionSchedule.listTerminated",model);
	}
	//예정경매 -> 진행경매 상태변경
	public void statusToProgress(int auctionScheduleNo) {
		Map model=Map.of("auctionScheduleNo",auctionScheduleNo);
		sqlSession.update("auctionSchedule.statusToProgress",model);
	}
	//진행경매 -> 종료경매 상태변경
	public void statusToTemination(int auctionScheduleNo) {
		Map model=Map.of("auctionScheduleNo",auctionScheduleNo);
		sqlSession.update("auctionSchedule.statusToTermination",model);
	}
	
	
	
	
}
