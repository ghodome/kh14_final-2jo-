package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // 방 생성 API
    @PostMapping("/create")
    public ResponseEntity<RoomChatDto> createRoom(@RequestHeader("Authorization") String token) {
        MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
        String memberId = claimVO.getMemberId(); // 토큰에서 멤버 ID 추출

        RoomChatDto roomChatDto = new RoomChatDto();
        roomChatDto.setRoomChatMemberId(memberId);
        
        // 방 생성 메서드 호출
        roomChatDao.insert(roomChatDto);
        
        // 생성된 방 반환
        return ResponseEntity.status(201).body(roomChatDto); // 201 Created
    }

  //목록
  	@GetMapping("/list")
  	public List<RoomChatDto> list() {
  		return roomChatDao.selectList();
  	}
}
