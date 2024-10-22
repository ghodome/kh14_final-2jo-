package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionDto {
	private int auctionNo;
	private int auctionScheduleNo;
	private int workNo;
	private String auctionSuccessBidder;
	private int auctionLot;
	private int auctionLowPrice;
	private int auctionHighPrice;
	private Timestamp auctionStartDate;
	private Timestamp auctionEndDate;
	private String auctionState;
	private int auctionHammerPrice;//낙찰가
	private int auctionPurchasePrice;//구매가
	private String auctionConsigner;//위탁자
	private int auctionConsignmentFee;//위탁수수료
	private int auctionNetProceeds;//위탁대금 (최종 낙찰 가격 )- (위탁 수수료 )- (기타 비용)
	private int auctionBidIncrement;//호가단위
	
}
