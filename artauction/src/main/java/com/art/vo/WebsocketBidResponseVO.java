package com.art.vo;

import lombok.Data;

@Data
public class WebsocketBidResponseVO {
	private final String type="bid";
	private String senderMemberId;
	private String senderMemberRank;
	private AuctionContentVO content;
	private String time;
}
