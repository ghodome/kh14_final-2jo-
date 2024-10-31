package com.art.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentApproveRequestVO {
	private List<DealWorkVO> dealList;
	private String partnerOrderId;
	private String tid;
	private String pgToken;
}
