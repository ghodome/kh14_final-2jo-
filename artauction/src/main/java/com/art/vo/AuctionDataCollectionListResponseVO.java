package com.art.vo;

import java.util.List;

import lombok.Data;

@Data
public class AuctionDataCollectionListResponseVO {
	private List<AuctionDataCollectionListVO> collectionList;
	private boolean isLast;
	private int count;
}
