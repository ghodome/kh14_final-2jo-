package com.art.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ChargeDto {
	private int ChargeNo;
	private String ChargeTid;
	private String ChargeName;
	private int ChargeTotal;
	private int ChargeRemain;
	private String memberId;
	private Date ChargeTime;
	
}
