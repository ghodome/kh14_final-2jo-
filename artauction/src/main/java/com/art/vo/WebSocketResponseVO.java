package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WebSocketResponseVO {
	private final String type = "chat";
	private String senderMemberId;//발신자
	private String senderMemberLevel;//발신자의 등급
	private String content;//보낸 내용
	private LocalDateTime time;//보낸 시각
}
