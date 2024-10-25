package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.RoomChatDao;
import com.art.dto.RoomChatDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;

@CrossOrigin
@RestController
@RequestMapping("/roomchat")
public class RoomChatRestController {

	@Autowired
	private RoomChatDao roomChatDao;

	@Autowired
	private TokenService tokenService;

	// 방생성 API
	@PostMapping("/create")
	public RoomChatDto createRoom(@RequestHeader("Authorization") String token) {
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		String memberId = claimVO.getMemberId();

		// 방이 존재하는지 확인
		List<RoomChatDto> sameRoom = roomChatDao.selectListByMemberId(memberId);

		if (!sameRoom.isEmpty()) {
			return sameRoom.get(0);
		}

		// 새 방 생성
		RoomChatDto roomChatDto = new RoomChatDto();
		roomChatDto.setRoomChatMemberId(memberId);

		roomChatDao.insert(roomChatDto);

		return roomChatDto;
	}

	// 목록
	@GetMapping("/list")
	public List<RoomChatDto> list() {
		return roomChatDao.selectList();
	}
}
