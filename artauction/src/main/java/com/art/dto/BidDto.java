package com.art.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BidDto {

	private int bidNo;
	private int auctionNo;
	private String memberId;
	private int bidPrice;
	private Date bidTime;
	private String bidContent;
}
