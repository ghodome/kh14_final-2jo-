package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.ChargeDto;
import com.art.dto.ChargeDetailDto;
import com.art.vo.ChargeTotalVO;


@Repository
public class ChargeDao {
	@Autowired
	private SqlSession sqlSession;
	
	public int chargeSequence() {
		return sqlSession.selectOne("charge.chargeSequence");
	}
	public int chargeDetailSequence() {
		return sqlSession.selectOne("charge.chargeDetailSequence");
	}
	public void chargeInsert(ChargeDto chargeDto) {
		 sqlSession.insert("charge.chargeInsert",chargeDto);
	}
	public void chargeDetailInsert(ChargeDetailDto chargeDetailDto) {
		sqlSession.insert("charge.chargeDetailInsert",chargeDetailDto);
	}
	public List<ChargeDto> selectList(String memberId) {
		return sqlSession.selectList("charge.list",memberId);
	}
	public ChargeDto selectOne(int chargeNo) {
		return sqlSession.selectOne("charge.find",chargeNo);
	}
	public List<ChargeDetailDto> selectDetailList(int chargeNo) {
		return sqlSession.selectList("charge.findDetail",chargeNo);
	}
	public List<ChargeTotalVO> selectTotalList(String memberId){
		return sqlSession.selectList("charge.findTotal",memberId);
	}
	//전체 취소
	public boolean cancelAll(int chargeNo) {
		return sqlSession.update("charge.cancelAll",chargeNo)>0;
	}
	public boolean cancelAllItem(int chargeNo) {
		return sqlSession.update("charge.cancelAllItem",chargeNo)>0;
	}
	//부분 취소
	public boolean cancelItem(int chargeDetailNo) {
		return sqlSession.update("charge.cancelItem",chargeDetailNo)>0;
	}
	public boolean decreaseItemRemain(int money,int chargeNo) {
		Map<String,Integer>params = Map.of("chargeNo",chargeNo,"money",money);
		return sqlSession.update("charge.decreaseItemRemain",params)>0;
	}
	public ChargeDetailDto selectDetailOne(int chargeDetailNo) {
		return sqlSession.selectOne("charge.selectDetailOne",chargeDetailNo);
	}
}
