package com.art.vo.pay;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayOrderResponseVO {
	private String tid;//거래 번호
	private String cid;
	private String status;
	private String partnerOrderId;//가맹점 주문번호
	private String partnerUserId;	//가맹점 회원 id
	private String paymentMethodType;//결제 수단, CARD 또는 MONEY 중 하나
	private KakaoPayAmountVO Amount;//	결제 금액
	private KakaoPayAmountVO canceledAmount;//취소된 금액
	private KakaoPayAmountVO cancelAvailableAmount;//	취소 가능 금액
	private String itemName;//	상품 이름, 최대 100자
	private String itemCode;//	상품 코드, 최대 100자
	private int quantity;//	상품 수량
	private LocalDateTime createdAt;//	결제 준비 요청 시각
	private LocalDateTime approvedAt;// 결제 승인 시각
	private LocalDateTime canceledAt;// 결제 취소 시각
	private KakaoPaySelectedCardInfoVO selectedCardInfo;//결제 카드 정보
	private List<KakaoPayPaymentActionDetailVO> paymentActionDetails;//결제&취소 상세
	
}
