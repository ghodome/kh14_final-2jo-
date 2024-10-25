package com.art.vo;

import java.util.List;

import com.art.advice.JsonEmptyStringToNullDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

//복합검색 요청을 위한 VO
@Data
public class MemberComplexRequestVO {
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String memberId;
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String memberName;
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String memberContact;
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String memberEmail;
//	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private Integer minMemberPoint;
//	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private Integer maxMemberPoint;
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String memberAddress;
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String beginMemberJoinDate, endMemberJoinDate;
//	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
	private String basicKeyword; 
	private String SearchColumn;
	private Integer beginRow, endRow;
	private List<String> memberRankList;
	private List<String> orderList;
}










