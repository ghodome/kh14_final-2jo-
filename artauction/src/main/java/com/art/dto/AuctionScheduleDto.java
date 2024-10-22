package com.art.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuctionScheduleDto {
	private int auctionScheduleNo;
	private String auctionScheduleTitle;
	private LocalDateTime auctionScheduleStartDate;
	private LocalDateTime auctionScheduleEndDate;
	private String auctionScheduleState;
}
