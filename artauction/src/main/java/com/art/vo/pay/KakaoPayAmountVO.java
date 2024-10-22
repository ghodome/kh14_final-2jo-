package com.art.vo.pay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayAmountVO {
	private int total;//전체 결제 금액
	private int taxFree;//비과세
	private int vat;//부과세
	private int point;//사용한 포인트
	private int discount;//할인 금액
	private int greenDeposit;//컵 보증금
}
