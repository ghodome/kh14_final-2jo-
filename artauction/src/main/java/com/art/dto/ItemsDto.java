package com.art.dto;

import lombok.Data;

@Data
public class ItemsDto {
	private int itemId;
	private String itemName;
	private int itemValue;
	private double chance;
	private String isWin;
	private int auctionNo;
}
