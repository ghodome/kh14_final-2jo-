package com.art.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionDataCollectionDto;
import com.art.dto.AuctionDto;
import com.art.vo.AuctionLotDetailVO;
import com.art.vo.AuctionLotVO;

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
	public List<AuctionLotVO> selectAuctionListWithJoin(int auctionScheduleNo) {
		return sqlSession.selectList("auction.scheduleListOrderByLot",auctionScheduleNo);
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
}
