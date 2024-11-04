package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionDataCollectionDto;
import com.art.dto.AuctionDto;
import com.art.vo.AuctionLotDetailVO;
import com.art.vo.AuctionLotListVO;
import com.art.vo.AuctionLotVO;
import com.art.vo.AuctionScheduleInfoVO;

@Repository
public class AuctionDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(AuctionDto auctionDto) {
		sqlSession.insert("auction.add",auctionDto);
	};
	public boolean update(AuctionDto auctionDto) {
		return sqlSession.update("auction.update",auctionDto)>0;
	}; 
	public boolean delete(int auctionNo) {
		return sqlSession.delete("auction.delete",auctionNo)>0;
	};
	public List<AuctionDto> selectList(){
		return sqlSession.selectList("auction.list");
	};
	public AuctionDto selectOne(int auctionNo) {
		return sqlSession.selectOne("auction.detail",auctionNo);
	};
	public List<AuctionDto> sceduleList(int auctionScheduleNo){
		return sqlSession.selectList("auction.sceduleList",auctionScheduleNo);
	};
	public List<AuctionDataCollectionDto> selectDataCollectionList(int auctionScheduleNo){
		return sqlSession.selectList("auctionData.list",auctionScheduleNo);
	};
	public int sequence() {
		return sqlSession.selectOne("auction.sequence");
		
	}
	//출품목록(사진x)
	public List<AuctionLotVO> selectAuctionListWithJoin(int auctionScheduleNo) {
		return sqlSession.selectList("auction.auctionListOrderByLot",auctionScheduleNo);
	}
	//출품목록(사진+페이징)
	public List<AuctionLotListVO> selectAuctionLotListWithJoin(int auctionScheduleNo) {
		return sqlSession.selectList("auction.auctionLotListOrderByLot",auctionScheduleNo);
	}
	//출품목록 일정 타이틀+종료날짜 불러오기
	public List<AuctionScheduleInfoVO> selectAuctionScheduleInfo(int auctionScheduleNo) {
		return sqlSession.selectList("auction.ScheduleInfo",auctionScheduleNo);
	}
	//출품작 상세 이미지 불러오기
	public List<AuctionLotListVO> selectAuctionWithImage(int auctionNo) {
		return sqlSession.selectList("auction.workImage",auctionNo);
	}
	
	public void cancelPresent(int auctionNo) {
		sqlSession.update("auction.cancelPresent",auctionNo);
	}
	public void uncancelPresent(int auctionNo) {
		sqlSession.update("auction.uncancelPresent",auctionNo);
		
	}
	public AuctionLotDetailVO selectAuctionWithWork(int auctionNo) {
		return sqlSession.selectOne("auction.selectAuctionWithWork",auctionNo);
	}
	
//	public boolean bidAvailable(int auctionNo,int bidPrice) {
//		Map map=Map.of("auctionNo",auctionNo,"bidPrice",bidPrice);
//		return sqlSession.selectOne("auction.selectOneByBidPrice",map)==0;
//	}
	public void selectBidInfo(int auctionNo) {
		
	}
	public List<Integer> selectListStarted(String time){
		Map data=Map.of("time",time);
		return sqlSession.selectList("auction.selectListStarted",data);
	}
	public List<Integer> selectListTerminated(String time){
		Map data=Map.of("time",time);
		return sqlSession.selectList("auction.selectListTerminated",data);
	}
	public void changeStateProgress(int auctionNo) {
		Map data=Map.of("auctionNo",auctionNo);
		sqlSession.update("auction.changeStateProgress",data);
	}
	public void changeStateTerminated(int auctionNo) {
		Map data=Map.of("auctionNo",auctionNo);
		sqlSession.update("auction.changeStateTerminated",data);
	}
	public void statusToProgress(Integer auctionScheduleNo) {
		Map data=Map.of("auctionScheduleNo",auctionScheduleNo);
		sqlSession.update("auction.statusToProgress",data);
	}

}
