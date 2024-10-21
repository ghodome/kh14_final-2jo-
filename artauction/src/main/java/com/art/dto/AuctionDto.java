package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionDto {
	private int auctionNo;
	private int auctionScheduleNo;
	private int workNo;
	private int auctionLowPrice;
	private int auctionHighPrice;
	private Timestamp auctionStartDate;
	private Timestamp auctionEndDate;
	private String auctionState;
	
}
