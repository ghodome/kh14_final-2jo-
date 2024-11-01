package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuctionScheduleListVO {
	private int auctionScheduleNo;
	private String auctionScheduleTitle;
	private LocalDateTime auctionScheduleStartDate;
	private LocalDateTime auctionScheduleEndDate;
	private String auctionScheduleState;
	private String auctionScheduleNotice;
    
    private Integer attachment;
}
