package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuctionLotDetailVO {
	private int auctionNo;
	private int auctionScheduleNo;
	private int workNo;
	private int auctionLot;
	private int auctionLowPrice;
	private int auctionHighPrice;
	private String auctionSuccessBidder;
	private LocalDateTime auctionStartDate;
	private LocalDateTime auctionEndDate;
	private int auctionHammerPrice;
	private int auctionPurchasePrice;
	private int auctionBidIncrement;
	private int auctionStartPrice;
	private int artistNo;
	private String workTitle;
	private String workDescription;
	private String workMaterials;
	private String workSize;
	private String workCategory;
	private String artistName;
	private String artistDescription;
	private String artistHistory;
	private String artistBirth;
	private String artistDeath;
	
}
