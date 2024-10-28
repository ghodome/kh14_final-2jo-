package com.art.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InventoryDto {
	private int inventoryId;
	private String memberId;
	private int itemId;
	private Date acquiredDate;
	private String itemName;
	private int itemValue;
}
