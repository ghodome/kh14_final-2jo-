package com.art.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.service.TokenService;

@RestController
@CrossOrigin
@RequestMapping("/auction")
public class AuctionWebSocketController {

//	@Autowired
//	private WebsocketMessageDao websocketMessageDao;
	
	@Autowired
	private TokenService tokenService;
}



 
