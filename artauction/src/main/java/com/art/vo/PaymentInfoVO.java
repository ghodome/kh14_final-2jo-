package com.art.vo;

import java.util.List;

import com.art.dto.PaymentDetailDto;
import com.art.dto.PaymentDto;
import com.art.vo.pay.KakaoPayOrderResponseVO;

import lombok.Data;

@Data
public class PaymentInfoVO {
	private PaymentDto paymentDto;
	private List<PaymentDetailDto> paymentDetailList;
	private KakaoPayOrderResponseVO responseVO;
}
