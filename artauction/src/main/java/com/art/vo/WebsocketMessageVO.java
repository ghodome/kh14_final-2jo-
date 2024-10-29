package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class WebsocketMessageVO {
	private int no; // 메시지 번호
	private String type; // 메시지 타입 (예: "chat", "dm" 등)
	private String senderMemberId; // 발신자 ID
	private String senderMemberLevel; // 발신자의 등급
	private String receiverMemberId; // 수신자 ID (null일 경우 전체 채팅)
	private String content; // 보낸 내용
	private LocalDateTime time; // 보낸 시각
}
