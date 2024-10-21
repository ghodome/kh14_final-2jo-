package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class AuctionDataCollectionDto {
	  	private int auctionNo;
	    private int auctionLowPrice;
	    private int auctionHighPrice;
	    private Timestamp auctionEndDate;
	    private String auctionState;
	    private int workNo;
	    private String workTitle;
	    private String workSize;
	    private String workCategory;
	    private int artistNo;
	    private String artistName;
}
