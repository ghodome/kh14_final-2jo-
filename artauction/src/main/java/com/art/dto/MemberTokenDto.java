package com.art.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class MemberTokenDto {
	private int tokenNo;
	private String tokenTarget;
	private String tokenValue;
	private Date tokenTime;
}
