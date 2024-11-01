package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NoticeDto {
	private int noticeNo;//글번호
//	private String noticeMemberId;//작성자
	private String noticeType;//글분류
	private String noticeTitle;//글제목
	private String noticeContent;//글내용
	private Timestamp noticeWtime;//게시일
}
