package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionScheduleDto {
	private int auctionScheduleNo;
	private String auctionScheduleTitle;
	private Timestamp auctionScheduleStartDate;
	private Timestamp auctionScheduleEndDate;
	private String auctionScheduleState;
}
