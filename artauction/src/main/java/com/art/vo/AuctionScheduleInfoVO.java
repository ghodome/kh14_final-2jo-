package com.art.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionScheduleInfoVO {
	private String auctionScheduleTitle;
	private Timestamp auctionScheduleEndDate;
}