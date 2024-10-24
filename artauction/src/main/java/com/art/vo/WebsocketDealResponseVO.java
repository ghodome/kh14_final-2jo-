package com.art.vo;

import lombok.Data;

@Data
public class WebsocketDealResponseVO {
	private final String type="deal";
	private String senderMemberId;
	private String senderMemberRank;
	private AuctionContentVO content;
	private String time;
}
