package com.art.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class BlockMemberDto {
	private int blockNo;
	private String blockType;
	private String blockMemberId;
	private String blockReason;
	private Date blockTime;
}
