package com.art.dto;

import lombok.Data;

@Data
public class ItemsDto {
	private int itemId;
	private String itemName;
	private int itemValue;
	private int chance;
	private String isWin;
}
