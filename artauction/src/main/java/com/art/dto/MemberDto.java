package com.art.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class MemberDto {
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberRank;
	private String memberEmail;
	private String memberContact;
	private String memberPost;
	private String memberAddress1;
	private String memberAddress2;
	private Date memberJoinDate;
	private int memberPoint;
}
