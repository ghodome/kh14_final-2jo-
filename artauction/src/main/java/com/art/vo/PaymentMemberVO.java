package com.art.vo;

import lombok.Data;

@Data
public class PaymentMemberVO {
	private int paymentDetailNo;
	private String paymentDetailName;
	private int paymentDetailPrice;
	private int paymentDetailQty;
	private int paymentDetailItem;
	private int paymentDetailOrigin;
	private String paymentDetailStatus;
	private String memberId;
}
