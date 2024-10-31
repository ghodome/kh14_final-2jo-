package com.art.vo;


import java.util.Date;
import java.util.List;

import com.art.dto.InventoryDto;


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
	//낙찰품
	private int dealNo;
	private int bidNo;
	private Date dealTime;
	private int dealPrice;
	private String dealStatus;
	//추가
	private String workTitle;
	private String artistName;
//	private List<InventoryDto> inventoryList;
//	private List<DealWorkVO> dealWorkList;
	

}
