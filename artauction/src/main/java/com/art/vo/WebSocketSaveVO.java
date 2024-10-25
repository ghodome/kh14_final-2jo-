package com.art.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WebSocketSaveVO {
	private String sender;
	private String level;
	private String content;
	private LocalDateTime time;
	private String receiverId;
}
