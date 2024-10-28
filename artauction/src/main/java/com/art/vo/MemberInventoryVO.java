package com.art.vo;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class MemberInventoryVO {
	//멤버
	private String memberId;
	private String memberName;
	private String memberRank;
	private String memberEmail;
	private String memberContact;
	private String memberPost;
	private String memberAddress1;
	private String memberAddress2;
	private int memberPoint;
	//인벤토리
	private int inventoryId;
	private int itemId;
	private Date acquiredDate;
	private String itemName;
	private int itemValue;
	
	

}
