package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuctionContentVO {
	private String content;
	private int bidNo;
	private int bidPrice;
	private String memberId;
	private LocalDateTime bidTime;
}
