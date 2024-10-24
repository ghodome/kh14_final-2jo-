package com.art.restcontroller;

import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.PaymentDao;
import com.art.dto.PaymentDto;
import com.art.error.TargetNotFoundException;
import com.art.service.KakaoPayService;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.PaymentApproveRequestVO;
import com.art.vo.PaymentPurchaseRequestVO;
import com.art.vo.pay.KakaoPayApproveRequestVO;
import com.art.vo.pay.KakaoPayApproveResponseVO;
import com.art.vo.pay.KakaoPayReadyRequestVO;
import com.art.vo.pay.KakaoPayReadyResponseVO;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentRestController {
	@Autowired
	private TokenService tokenService;
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@Autowired
	private PaymentDao paymentDao;
	
	//구매
		@PostMapping("/purchase")
		public KakaoPayReadyResponseVO purchase(
				@RequestBody PaymentPurchaseRequestVO request
//				,@RequestHeader("Authorization")String token
				) throws URISyntaxException {
			//카카오페이에 보낼 최종 결제 정보를 생성
			//결제 준비 요청을 보내고
			//사용자에게 필요한 정보를 전달
			MemberClaimVO claimVO = tokenService.check("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzIyNjI0ODAsImlzcyI6IktIYWNhZGVteSIsImlhdCI6MTcyOTU4NDA4MCwibWVtYmVySWQiOiJ0ZXN0dXNlcjEiLCJtZW1iZXJSYW5rIjoi7ZqM7JuQIn0.1xsMvbxvjMMSLq239HtLBBj3CIxmi-J-MLtQ0obkEm0");
			int total = (int) (request.getTotalAmount() * 0.7);
			KakaoPayReadyRequestVO requestVO = new KakaoPayReadyRequestVO();
			requestVO.setPartnerOrderId(UUID.randomUUID().toString());
			requestVO.setPartnerUserId(claimVO.getMemberId());
			requestVO.setItemName("모나리자");
//			requestVO.setItemName(request.getItemName());
			requestVO.setTotalAmount(total);
			requestVO.setApprovalUrl(request.getApprovalUrl());
			requestVO.setCancelUrl(request.getCancelUrl());
			requestVO.setFailUrl(request.getFailUrl());
			KakaoPayReadyResponseVO responseVO = kakaoPayService.ready(requestVO);
			return responseVO;
		}
		@Transactional
		@PostMapping("/approve")
		public KakaoPayApproveResponseVO approve(
//				@RequestHeader("Authorization")String token,
				@RequestBody PaymentApproveRequestVO request) throws URISyntaxException {
			KakaoPayApproveRequestVO requestVO = new KakaoPayApproveRequestVO();
//			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
			MemberClaimVO claimVO = tokenService.check("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzIyNjI0ODAsImlzcyI6IktIYWNhZGVteSIsImlhdCI6MTcyOTU4NDA4MCwibWVtYmVySWQiOiJ0ZXN0dXNlcjEiLCJtZW1iZXJSYW5rIjoi7ZqM7JuQIn0.1xsMvbxvjMMSLq239HtLBBj3CIxmi-J-MLtQ0obkEm0");
			requestVO.setPartnerOrderId(request.getPartnerOrderId());
			requestVO.setPartnerUserId(claimVO.getMemberId());
			requestVO.setTid(request.getTid());
			requestVO.setPgToken(request.getPgToken());
			KakaoPayApproveResponseVO responseVO = kakaoPayService.approve(requestVO);
			//최종 결제가 완료된 시점에 DB에 결제에 대한 기록을 남긴다
			
			//[1] 대표 정보 등록
			int paymentSeq = paymentDao.paymentSequence();
			PaymentDto paymentDto = new PaymentDto();
			paymentDto.setPaymentNo(paymentSeq);//결제번호
			paymentDto.setPaymentTid(responseVO.getTid());//거래번호
			paymentDto.setPaymentName(responseVO.getItemName());//거래상품명
			paymentDto.setPaymentTotal(responseVO.getAmount().getTotal());//거래금액
			paymentDto.setPaymentRemain(paymentDto.getPaymentTotal());//취소가능금액
			paymentDto.setMemberId(claimVO.getMemberId());//구매자ID
			paymentDao.paymentInsert(paymentDto);
			//[2] 결제 상세 정보 등록
//			for(BookQtyVO qtyVO : request.getBookList()) {
//				BookDto bookDto = bookDao.selectOne(qtyVO.getBookId());
//				if(bookDto==null)throw new TargetNotFoundException("존재하지 않는 도서");
//				int paymentDetailSql = paymentDao.paymentDetailSequence();
//				PaymentDetailDto paymentDetailDto =new PaymentDetailDto();
//				paymentDetailDto.setPaymentDetailNo(paymentDetailSql);//번호 설정
//				paymentDetailDto.setPaymentDetailName(bookDto.getBookTitle());//상품명
//				paymentDetailDto.setPaymentDetailPrice(bookDto.getBookPrice());//상품판매
//				paymentDetailDto.setPaymentDetailItem(bookDto.getBookId());//상품번호
//				paymentDetailDto.setPaymentDetailQty(qtyVO.getQty());//구매수량
//				paymentDetailDto.setPaymentDetailOrigin(paymentSeq);//결제대표번호
//				paymentDao.paymentDetailInsert(paymentDetailDto);
//			}
			return responseVO;
		}
		
}
