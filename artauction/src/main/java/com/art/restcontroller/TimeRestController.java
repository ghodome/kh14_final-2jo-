package com.art.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.service.TimeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/time")
public class TimeRestController {

	@Autowired
	private TimeService timeService;
	
	@GetMapping("/")
	public String getTime() {
		String timeNow=timeService.getTime();
		log.info("시간 요청! 현재시각 = {}",timeNow);
		return timeNow;
	}
}
