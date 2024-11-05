package com.art.vo.pay;

import lombok.Data;

@Data
public class PaymentDetailVO {
	private int paymentDetailNo;
	private String paymentDetailName;
	private long paymentDetailPrice;
	private int paymentDetailOrigin;
	private String memberId;
}
