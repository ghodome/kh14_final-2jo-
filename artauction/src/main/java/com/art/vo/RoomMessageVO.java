package com.art.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoomMessageVO {
	 private String sender;   
	    private String receiver; 
	    private String content; 
	    private Timestamp time;
}
