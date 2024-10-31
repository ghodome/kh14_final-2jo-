package com.art.restcontroller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.DealDao;
import com.art.dao.PaymentDao;
import com.art.dto.DealDto;
import com.art.dto.PaymentDetailDto;
import com.art.dto.PaymentDto;
import com.art.error.TargetNotFoundException;
import com.art.service.KakaoPayService;
import com.art.service.TokenService;
import com.art.vo.DealWorkVO;
import com.art.vo.MemberClaimVO;
import com.art.vo.PaymentApproveRequestVO;
import com.art.vo.PaymentMemberVO;
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
	@Autowired
	private DealDao dealDao;
	//구매
		@PostMapping("/purchase")
		public KakaoPayReadyResponseVO purchase(
				@RequestBody PaymentPurchaseRequestVO request
				,@RequestHeader("Authorization")String token
				) throws URISyntaxException {
			//카카오페이에 보낼 최종 결제 정보를 생성
			//결제 준비 요청을 보내고
			//사용자에게 필요한 정보를 전달
			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
			StringBuffer buffer = new StringBuffer();
			int total = 0;
			for(DealWorkVO vo : request.getDealList()) {
				DealWorkVO dealWorkVO = dealDao.selectThis(vo.getDealNo());
				if(dealWorkVO==null)throw new TargetNotFoundException("결제 대상 없음");
				total += dealWorkVO.getDealPrice();
				if(buffer.isEmpty()) {
					buffer.append(dealWorkVO.getWorkTitle());
				}
			}
			if(request.getDealList().size()>=2) {
				buffer.append("외 "+(request.getDealList().size()-1)+"건");
			}
			
			KakaoPayReadyRequestVO requestVO = new KakaoPayReadyRequestVO();
			requestVO.setPartnerOrderId(UUID.randomUUID().toString());
			requestVO.setPartnerUserId(claimVO.getMemberId());
			requestVO.setItemName(buffer.toString());
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
				@RequestHeader("Authorization")String token,
				@RequestBody PaymentApproveRequestVO request) throws URISyntaxException {
			dealDao.updateStatusSuccess(request.getDealList().get(0).getDealNo());
			KakaoPayApproveRequestVO requestVO = new KakaoPayApproveRequestVO();
			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
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
				for(DealWorkVO vo : request.getDealList()) {
					DealWorkVO dealWorkVO = dealDao.selectThis(vo.getDealNo());
				if(dealWorkVO==null)throw new TargetNotFoundException("존재하지 않는 상품");
				int paymentDetailSql = paymentDao.paymentDetailSequence();
				PaymentDetailDto paymentDetailDto =new PaymentDetailDto();
				paymentDetailDto.setPaymentDetailName(dealWorkVO.getWorkTitle());//상품명
				paymentDetailDto.setPaymentDetailNo(paymentDetailSql);//번호 설정
				paymentDetailDto.setPaymentDetailPrice(dealWorkVO.getDealPrice());//상품판매
				paymentDetailDto.setPaymentDetailItem(dealWorkVO.getDealNo());//상품번호
				paymentDetailDto.setPaymentDetailQty(1);//구매수량
				paymentDetailDto.setPaymentDetailOrigin(paymentSeq);//결제대표번호
				paymentDao.paymentDetailInsert(paymentDetailDto);					
				}
				
			return responseVO;
		}
		@GetMapping("/rank")
		public List<PaymentMemberVO> buyRank(){
			List<PaymentMemberVO> list =paymentDao.selectRankList();
			return list;
		}
		
}
