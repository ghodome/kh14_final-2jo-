package com.art.vo;

import java.util.List;

import lombok.Data;

@Data
public class AuctionListResponseVO {
	private List<AuctionListVO> auctionList;
	private boolean isLast;
	private int count;
}
