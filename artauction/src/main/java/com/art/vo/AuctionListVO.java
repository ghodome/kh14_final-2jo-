package com.art.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionListVO {
	private int auctionNo;
	private int auctionScheduleNo;
	private int workNo;
	private int auctionLot;
	private int auctionLowPrice;
	private int auctionHighPrice;
	private String auctionState;
	private String auctionConsigner;
	private int auctionConsignmentFee;
	private int auctionStartPrice;
	private int auctionBidPrice;
	private int auctionBidIncrement;
	private Timestamp auctionStartDate;
	private Timestamp auctionEndDate;
	private String auctionSuccessBidder;
	private int auctionHammerPrice;
	private int auctionBidCnt;
	private int auctionNetProceeds;
	
	private Integer attachment;
}
