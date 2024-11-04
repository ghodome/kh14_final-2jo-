package com.art.vo;

import java.util.List;

import lombok.Data;

@Data
public class AuctionScheduleListResponseVO {
	private List<AuctionScheduleListVO> auctionScheduleList;
	private boolean isLast;
	private int count;

}