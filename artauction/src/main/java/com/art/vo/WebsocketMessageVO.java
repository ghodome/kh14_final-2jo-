package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WebsocketMessageVO {
	private int no;
	private String type;
	private String senderMemberId;//발신자
	private String senderMemberLevel;//발신자의 등급
	private String receiverMemberId;//수신자
	private String content;//보낸 내용
	private LocalDateTime time;//보낸 시각
}