package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoomChatDto {
	private int roomChatNo;
	private String roomChatMemberId;
	private Timestamp roomChatCreated;
	
}
