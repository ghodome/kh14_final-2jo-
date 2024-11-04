package com.art.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AuctionLotListVO {
	private int auctionNo;
	private int auctionLot;
	private String auctionState; 
	private int auctionLowPrice;
	private int auctionHighPrice;
	private Timestamp auctionEndDate;
	private String workTitle;
	private String workSize;
	private String workMaterials;
	private String workCategory;
	private Integer attachment;
}