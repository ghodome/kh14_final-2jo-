package com.art.vo;

import lombok.Data;

@Data
public class WebsocketBidRequestVO {
	private String type;
	private WebsocketAuctionBidVO bid;
}
