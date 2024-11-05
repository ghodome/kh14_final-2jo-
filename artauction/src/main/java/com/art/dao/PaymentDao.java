package com.art.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.art.dto.PaymentDetailDto;
import com.art.dto.PaymentDto;
import com.art.vo.PaymentMemberVO;
import com.art.vo.PaymentTotalVO;
import com.art.vo.pay.PaymentDetailVO;


@Repository
public class PaymentDao {
	@Autowired
	private SqlSession sqlSession;
	
	public int paymentSequence() {
		return sqlSession.selectOne("payment.paymentSequence");
	}
	public int paymentDetailSequence() {
		return sqlSession.selectOne("payment.paymentDetailSequence");
	}
	public void paymentInsert(PaymentDto paymentDto) {
		 sqlSession.insert("payment.paymentInsert",paymentDto);
	}
	public void paymentDetailInsert(PaymentDetailDto paymentDetailDto) {
		sqlSession.insert("payment.paymentDetailInsert",paymentDetailDto);
	}
	public List<PaymentDto> selectList(String memberId) {
		return sqlSession.selectList("payment.list",memberId);
	}
	public PaymentDto selectOne(int paymentNo) {
		return sqlSession.selectOne("payment.find",paymentNo);
	}
	public List<PaymentDetailDto> selectDetailList(int paymentNo) {
		return sqlSession.selectList("payment.findDetail",paymentNo);
	}
	public List<PaymentTotalVO> selectTotalList(String memberId){
		return sqlSession.selectList("payment.findTotal",memberId);
	}
	//전체 취소
	public boolean cancelAll(int paymentNo) {
		return sqlSession.update("payment.cancelAll",paymentNo)>0;
	}
	public boolean cancelAllItem(int paymentNo) {
		return sqlSession.update("payment.cancelAllItem",paymentNo)>0;
	}
	//부분 취소
	public boolean cancelItem(int paymentDetailNo) {
		return sqlSession.update("payment.cancelItem",paymentDetailNo)>0;
	}
	public boolean decreaseItemRemain(int money,int paymentNo) {
		Map<String,Integer>params = Map.of("paymentNo",paymentNo,"money",money);
		return sqlSession.update("payment.decreaseItemRemain",params)>0;
	}
	public PaymentDetailDto selectDetailOne(int paymentDetailNo) {
		return sqlSession.selectOne("payment.selectDetailOne",paymentDetailNo);
	}
	public List<PaymentMemberVO> selectRankList(){
		return sqlSession.selectList("payment.rankList");
	}
	public List<PaymentDetailVO> selectDetailIdList(){
		return sqlSession.selectList("payment.detailId");
	}
	public PaymentDetailDto findDetailOne(int paymentDetailNo) {
		return sqlSession.selectOne("payment.findDetailOne",paymentDetailNo);
	}
	public void updateDetailStatus(int paymentDetailNo) {
		sqlSession.update("payment.updateDetailStatus",paymentDetailNo);
		
	}
	public void updatePaymentRemain(PaymentDto paymentDto) {
		sqlSession.update("payment.updatePaymentRemain",paymentDto);
	}
}
