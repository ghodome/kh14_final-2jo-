package com.art.vo;

import lombok.Data;

@Data
public class AuctionLotVO {
	private int auctionNo;
	private int auctionLot;
	private String auctionState; 
	private String workTitle;
	private String workMaterials;
	private String workCategory;
}
