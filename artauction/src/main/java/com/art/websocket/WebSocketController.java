package com.art.websocket;



import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.art.vo.WebSocketSaveVO;
import com.art.vo.WebSocketSendVO;

@Controller
public class WebSocketController {

	@MessageMapping("/chat")
	@SendTo("/public/chat")
	public WebSocketSaveVO chat(WebSocketSendVO send) {
		WebSocketSaveVO save = new WebSocketSaveVO();
		save.setContent(send.getContent());
		save.setTime(LocalDateTime.now());
		return save;
	}
}
