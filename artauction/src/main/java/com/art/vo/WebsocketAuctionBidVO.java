package com.art.vo;

import lombok.Data;

@Data
public class WebsocketAuctionBidVO {
	private int bidPrice;
	private int bidIncrement;
	private int auctionLot;
	private String workName;
}
