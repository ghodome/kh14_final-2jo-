package com.art.service;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

@Service
public class TimeService {

	public String getTime() {
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	long timeNow=System.currentTimeMillis();
    	String timeToString=fmt.format(timeNow);
		return timeToString; 
	};
	
}
