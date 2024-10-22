package com.art.vo;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberPurchaseRequestVO {
	private int totalAmount;
	private String approvalUrl;
	private String cancelUrl;
	private String failUrl;
}
