package com.art.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentApproveRequestVO {
	private String partnerOrderId;
	private String tid;
	private String pgToken;
}
