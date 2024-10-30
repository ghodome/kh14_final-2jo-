package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.BidDto;

@Repository
public class BidDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(BidDto bidDto) {
		sqlSession.insert("bid.insert",bidDto);
	}
	public int sequence() {
		return sqlSession.selectOne("bid.sequence");
	}
	public List<BidDto> getBidList(){
		return sqlSession.selectList("bid.getList");
	}
	public List<BidDto> getBidListByAuctionNo(int auctionNo){
		Map data=Map.of("auctionNo",auctionNo);
		return sqlSession.selectList("bid.getListByAuctionNo",data);
	}
}
