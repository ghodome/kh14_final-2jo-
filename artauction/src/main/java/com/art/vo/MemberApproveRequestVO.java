package com.art.vo;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberApproveRequestVO {
	private String partnerOrderId;
	private String partnerUserId;
	private String tid;
	private String pgToken;
}
