package com.art.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class MemberBlockVO {
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberBirth;
	private String memberContact;
	private String memberEmail;
	private String memberRank;
	private int memberPoint;
	private String memberPost;
	private String memberAddress1, memberAddress2;
	private Date memberJoinDate;
	private int blockNo;
	private String blockType;
	private String blockMemberId;
	private String blockReason;
	private Date blockTime;
}
