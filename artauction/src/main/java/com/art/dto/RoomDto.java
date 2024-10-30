package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoomDto {
	private int roomNo;
	private String roomName;
	private Timestamp roomCreated;
}
