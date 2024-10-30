package com.art.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChatDto {
    private int chatNo;
    private String chatType;
    private String chatSender; 	
    private String chatReceiver; 
    private String chatContent; 
    private Timestamp chatTime; 
}