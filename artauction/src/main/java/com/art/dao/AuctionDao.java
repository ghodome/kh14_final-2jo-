package com.art.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.AuctionDataCollectionDto;
import com.art.dto.AuctionDto;
import com.art.vo.AuctionDataCollectionListRequestVO;
import com.art.vo.AuctionDataCollectionListVO;
import com.art.vo.AuctionLotDetailVO;

@Repository
public class AuctionDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public int sequence() {
		return sqlSession.selectOne("auction.sequence");
	}
	public void insert(AuctionDto auctionDto) {
		sqlSession.insert("auction.add", auctionDto);
	}
	
	public boolean update(AuctionDto auctionDto) {
		return sqlSession.update("auction.update",auctionDto)>0;
	}
	
	public boolean delete(int auctionNo) {
		return sqlSession.delete("auction.delete", auctionNo)>0;
	}
	
	//이미지 삭제
	public boolean deleteImage(int auctionNo) {
		return sqlSession.delete("auction.deleteImage", auctionNo) > 0;
	}
	
	//이미지 찾기
	public List<Integer> findImages(int auctionNo) {	
		return sqlSession.selectList("auction.findImages", auctionNo);
	}
	
	public List<AuctionDto> selectList(){
		return sqlSession.selectList("auction.list");
	}
	
	public AuctionDto selectOne(int auctionNo) {
		return sqlSession.selectOne("auction.detail",auctionNo);
	}
	
	//출품상세(이미지포함)
	public AuctionDataCollectionListVO selectOneImage(int auctionNo) {
		return sqlSession.selectOne("auction.detailall", auctionNo);
	}
	
	public List<AuctionDto> sceduleList(int auctionScheduleNo){
		return sqlSession.selectList("auction.sceduleList",auctionScheduleNo);
	}
	
	public List<AuctionDataCollectionListVO> selectDataCollectionList(int auctionScheduleNo){
		return sqlSession.selectList("auctionData.list",auctionScheduleNo);
	}
	
	
	public List<AuctionDataCollectionDto> selectAuctionListWithJoin(int auctionScheduleNo) {
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
	
	//이미지 연결기능
	public void connect(int work, int attachment) {
		Map<String, Object> params = new HashMap<>();
		params.put("work", work);
		params.put("attachment", attachment);
		sqlSession.insert("auctionData.connect", params);
	}
	
//	public boolean bidAvailable(int auctionNo,int bidPrice) {
//		Map map=Map.of("auctionNo",auctionNo,"bidPrice",bidPrice);
//		return sqlSession.selectOne("auction.selectOneByBidPrice",map)==0;
//	}
	public void selectBidInfo(int auctionNo) {
		
	}
	
	//목록 페이징
	public int countWithPaging(AuctionDataCollectionListRequestVO listRequestVO) {
		return sqlSession.selectOne("auctionData.count", listRequestVO);
	}
	//목록 페이징+검색
	public List<AuctionDataCollectionListVO> selectListByPaging(AuctionDataCollectionListRequestVO listRequestVO) {
		return sqlSession.selectList("auctionData.list", listRequestVO);
	}
}
