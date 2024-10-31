package com.art.vo;

import java.util.List;


import lombok.Data;

@Data
public class PaymentPurchaseRequestVO {
	private List<DealWorkVO> dealList;
	private int totalAmount;
	private String approvalUrl;
	private String cancelUrl;
	private String failUrl;
}
