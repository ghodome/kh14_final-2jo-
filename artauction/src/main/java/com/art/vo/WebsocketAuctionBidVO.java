package com.art.vo;

import lombok.Data;

@Data
public class WebsocketAuctionBidVO {
	private int bidPrice;
	private int hammerPrice;
	private int bidIncrement;
}
