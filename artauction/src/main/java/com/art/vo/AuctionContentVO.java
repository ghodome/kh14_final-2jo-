package com.art.vo;

import lombok.Data;

@Data
public class AuctionContentVO {
	private String content;
	private int bidNo;
	private int bidPrice;
	private String memberId;
	private String bidTime;
}
