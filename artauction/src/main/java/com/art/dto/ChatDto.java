package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChatDto {
	private int chatNo;
	private String chatMemberIdSend;
	private String chatMemberIdReceiver;
	private int roomChatNo;
	private String chatContnet;
	private Timestamp chatTime;
}
