package com.art.vo;

import lombok.Data;

@Data
public class AuctionContentVO {
	private String contentForSchedule;//
	private String contentForLot;
	private int auctionLot;
	private int auctionNo;
	private int bidPrice;//
	private String memberId;//
	private String bidTime;
	private int bidCnt;
}
