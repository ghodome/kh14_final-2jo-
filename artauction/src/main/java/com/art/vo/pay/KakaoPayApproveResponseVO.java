package com.art.vo.pay;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

//카카오페이 결제 승인 응답 데이터
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayApproveResponseVO {
	private String aid;//요청 번호
	private String tid;//거래 번호
	private String cid;//가맹점 번호
	private String sid;//정기결제 번호
	private String partnerOrderId;//가맹점 내 거래번호
	private String partnerUserId;//가맹점 내 구매자 ID
	private String paymentMethodType;//결제 유형
	private KakaoPayAmountVO amount;//금액 정보
	private KakaoPayCardInfoVO cardInfo;//카드 정보
	private String itemName;//상품 이름
	private String itemCode;//상품 코드
	private int quantity;//상품 수량
	private LocalDateTime createdAt;//결제 준비 요청 시각
	private LocalDateTime approvedAt;//결제 승인 시각
	private String payload;//결제승인 요청에 대해 저장한 값 
}
