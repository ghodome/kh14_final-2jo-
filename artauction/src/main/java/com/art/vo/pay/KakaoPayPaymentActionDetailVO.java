package com.art.vo.pay;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayPaymentActionDetailVO {
	private String aid;//Request 고유 번호
	private LocalDateTime approvedAt;//거래시간
	private int amount;//결제&취소 총액
	private int pointAmount;//결제&취소 포인트 금액
	private int discountAmount;//할인 금액
	private int greenDeposit;//	컵 보증금
	private String paymentActionType;//결제 타입 PAYMENT(결제), CANCEL(결제취소), ISSUED_SID(SID 발급) 중 하나
	private String payload;//lRequest로 전달한 값
}
