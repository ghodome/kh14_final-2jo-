package com.art.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.DealDto;
import com.art.vo.DealWorkVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DealDao {
	@Autowired
	private SqlSession sqlSession;
	
	public List<DealWorkVO> list(String memberId){
		return sqlSession.selectList("deal.list",memberId);
	}
	public DealDto selectOne(int dealNo) {
		return sqlSession.selectOne("deal.detail",dealNo);
	}
	public DealWorkVO selectThis(int dealNo) {
		return sqlSession.selectOne("deal.detailVO",dealNo);
	}
	public void updateStatus(int dealNo) {
		sqlSession.update("deal.updateStatus",dealNo);
	}
	public void updateStatusSuccess(int dealNo) {
		sqlSession.update("deal.updateStatusSuccess",dealNo);
	}
	public void insert(DealDto dealDto) {
		sqlSession.insert("deal.insert",dealDto);
	}
	public List<DealWorkVO> selectGG() {
		return sqlSession.selectList("deal.detailGG");
	}
	public boolean insertByAuction(Integer auctionNo) {
		Map data = Map.of("auctionNo",auctionNo);
		log.info("낙찰 작성 중");
		return sqlSession.insert("deal.insertByAuction",data)>0;
	}
}
