package com.art.vo;

import java.util.List;

import com.art.dto.PaymentDetailDto;
import com.art.dto.PaymentDto;

import lombok.Data;

@Data 
public class PaymentTotalVO {
	private PaymentDto paymentDto;
	private List<PaymentDetailDto> paymentDetailList;
}
