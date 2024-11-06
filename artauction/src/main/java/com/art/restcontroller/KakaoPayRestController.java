package com.art.restcontroller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.PaymentDao;
import com.art.dto.PaymentDetailDto;
import com.art.dto.PaymentDto;
import com.art.error.TargetNotFoundException;
import com.art.service.KakaoPayService;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.PaymentInfoVO;
import com.art.vo.pay.KakaoPayApproveRequestVO;
import com.art.vo.pay.KakaoPayApproveResponseVO;
import com.art.vo.pay.KakaoPayCancelRequestVO;
import com.art.vo.pay.KakaoPayCancelResponseVO;
import com.art.vo.pay.KakaoPayOrderRequestVO;
import com.art.vo.pay.KakaoPayOrderResponseVO;
import com.art.vo.pay.KakaoPayReadyRequestVO;
import com.art.vo.pay.KakaoPayReadyResponseVO;

@CrossOrigin
@RestController
@RequestMapping("/kakaopay")
public class KakaoPayRestController {
	
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private PaymentDao paymentDao;
	
	
	@PostMapping("/ready")
	public KakaoPayReadyResponseVO ready(
			@RequestBody KakaoPayReadyRequestVO request,
			@RequestHeader("Authorization") String token) throws URISyntaxException {
		
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		
		request.setPartnerOrderId(UUID.randomUUID().toString());
		request.setPartnerUserId(claimVO.getMemberId());
		KakaoPayReadyResponseVO response = kakaoPayService.ready(request);
		return response;
	}
	@PostMapping("/approve")
	public KakaoPayApproveResponseVO approve(
			 @RequestHeader("Authorization") String token,
			 @RequestBody KakaoPayApproveRequestVO request) throws URISyntaxException {
		MemberClaimVO claimVO = 
				tokenService.check(tokenService.removeBearer(token));
		request.setPartnerUserId(claimVO.getMemberId());
		KakaoPayApproveResponseVO response = kakaoPayService.approve(request);
		return response;
	}
	@GetMapping("/order/{tid}")
	public KakaoPayOrderResponseVO order(@PathVariable String tid) throws URISyntaxException {
		KakaoPayOrderRequestVO request = new KakaoPayOrderRequestVO();
		request.setTid(tid);
		return kakaoPayService.order(request);
	}
	@GetMapping("/detail/{paymentNo}")
	public PaymentInfoVO detail(@PathVariable int paymentNo,
			@RequestHeader("Authorization")String token) throws URISyntaxException{
		//결제내역
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto==null)throw new TargetNotFoundException("존재하지 않는 결제내역");
		//본인 검증
		if(!claimVO.getMemberId().equals(paymentDto.getMemberId()))throw new TargetNotFoundException("결제 내역 소유자가 아닙니다.");
		List<PaymentDetailDto> list = paymentDao.selectDetailList(paymentNo);
		//카카오페이 조회 내역
		KakaoPayOrderRequestVO requestVO = new KakaoPayOrderRequestVO();
		requestVO.setTid(paymentDto.getPaymentTid());
		KakaoPayOrderResponseVO responseVO = kakaoPayService.order(requestVO);

		PaymentInfoVO paymentInfoVO = new PaymentInfoVO();
		paymentInfoVO.setPaymentDto(paymentDto);
		paymentInfoVO.setPaymentDetailList(list);
		paymentInfoVO.setResponseVO(responseVO);
		return paymentInfoVO;
	}
	//취소는 두개를 만들어야 한다
	//1. 전체 취소(paymentNo 이용)
	//2. 항목 취소(paymentDetailNo 이용)
	@Transactional
	@DeleteMapping("/cancelAll/{paymentNo}")
	public KakaoPayCancelResponseVO cancelAll(
			@PathVariable int paymentNo,
			@RequestHeader("Authorization") String token) throws URISyntaxException {
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto == null) 
			throw new TargetNotFoundException("존재하지 않는 결제정보");
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		if(paymentDto.getMemberId().equals(claimVO.getMemberId()) == false)
			throw new TargetNotFoundException("소유자 불일치");
		if(paymentDto.getPaymentRemain() == 0)
			throw new TargetNotFoundException("이미 취소된 결제");
		
		//[1] 카카오페이에 해당 결제 거래번호에 대한 남은금액을 취소해달라고 요청
		KakaoPayCancelRequestVO request = new KakaoPayCancelRequestVO();
		request.setTid(paymentDto.getPaymentTid());
		request.setCancelAmount(paymentDto.getPaymentRemain());
		KakaoPayCancelResponseVO response = kakaoPayService.cancel(request);
		
		//[2] payment 테이블의 잔여금액을 0으로 변경
		paymentDao.cancelAll(paymentNo);
		//[3] payment_detail 테이블의 관련항목의 상태를 취소로 변경
		paymentDao.cancelAllItem(paymentNo);
		
		return response;
	}
	@Transactional
	@DeleteMapping("/cancelItem/{paymentDetailNo}")
	public KakaoPayCancelResponseVO cancelItem(
			@RequestHeader("Authorization")String token,
			@PathVariable int paymentDetailNo) throws URISyntaxException {
		PaymentDetailDto paymentDetailDto = 
				paymentDao.selectDetailOne(paymentDetailNo);
		if(paymentDetailDto==null)throw new TargetNotFoundException("존재하지 않는 결제정보");
		
		PaymentDto paymentDto = paymentDao.selectOne(paymentDetailDto.getPaymentDetailOrigin());
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		if(!paymentDto.getMemberId().equals(claimVO.getMemberId()))
			throw new TargetNotFoundException("소유자 불일치");
		//[1]카카오페이 취소
		int money = paymentDetailDto.getPaymentDetailPrice() * paymentDetailDto.getPaymentDetailQty();
		KakaoPayCancelRequestVO request = new KakaoPayCancelRequestVO();
		request.setTid(paymentDto.getPaymentTid());
		request.setCancelAmount(money);
		KakaoPayCancelResponseVO response = kakaoPayService.cancel(request);
		//[2]결제 상세 테이블의 해당 항목의 상태를 취소로 변경
		paymentDao.cancelItem(paymentDetailNo);
		//[3]결제 대표 테이블의 잔여 금액을 해당 항목의 금액만큼 차감
		paymentDao.decreaseItemRemain(money, paymentDto.getPaymentNo());
		return response;
	}
	
}
