package com.art.vo;

import lombok.Data;

@Data
public class PaymentPurchaseRequestVO {
	private int totalAmount;
	private String approvalUrl;
	private String cancelUrl;
	private String failUrl;
}
