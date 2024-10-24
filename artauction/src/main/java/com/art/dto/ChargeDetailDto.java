package com.art.dto;

import lombok.Data;

@Data
public class ChargeDetailDto {
	private int chargeDetailNo;
	private String chargeDetailName;
	private int chargeDetailPrice;
	private int chargeDetailQty;
	private int chargeDetailItem;
	private int chargeDetailOrigin;
	private String chargeDetailStatus;
}
