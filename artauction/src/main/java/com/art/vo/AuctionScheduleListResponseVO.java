package com.art.vo;

import java.util.List;

import com.art.dto.AuctionScheduleDto;

import lombok.Data;

@Data
public class AuctionScheduleListResponseVO {
	private List<AuctionScheduleDto> auctionScheduleList;
	private boolean isLast;
	private int count;

}
