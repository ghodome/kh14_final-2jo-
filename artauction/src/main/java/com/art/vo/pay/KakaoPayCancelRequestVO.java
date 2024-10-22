package com.art.vo.pay;

import lombok.Data;

@Data
public class KakaoPayCancelRequestVO {
	private String tid; //결제 고유번호, 20자
	private int cancelAmount;//취소 금액
	private int cancelTaxFreeAmount=0;//취소 비과세 금액
}
