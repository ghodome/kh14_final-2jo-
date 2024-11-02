	package com.art.restcontroller;
	
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.RoomDao;
import com.art.dto.RoomDto;
import com.art.dto.RoomMemberDto;
import com.art.error.TargetNotFoundException;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.PaymentMemberVO;
import com.art.vo.RoomVO;

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomRestController {

	@Autowired
	private RoomDao roomDao;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/")
	public RoomDto insert(@RequestBody RoomDto roomDto,
	        @RequestHeader("Authorization") String token) {
	    // 토큰에서 회원 정보 추출
	    MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
	    String memberId = claimVO.getMemberId();

	    // 방 목록을 가져와서 같은 이름의 방이 있는지 확인
	    List<RoomDto> existingRooms = roomDao.selectList();
	    boolean roomExists = false;

	    for (RoomDto existingRoom : existingRooms) {
	        if (existingRoom.getRoomName().equals(memberId)) {
	            roomExists = true;
	            break;
	        }
	    }

	    if (roomExists) {
	        throw new TargetNotFoundException("이미 존재하는 방입니다.");
	    }

	    // 방 생성
	    int roomNo = roomDao.sequence();
	    roomDto.setRoomNo(roomNo);
	    roomDto.setRoomName(memberId); // 방 이름을 회원 아이디로 설정
	    roomDao.insert(roomDto);

	    return roomDao.selectOne(roomNo); // DB에서 만든 정보까지 포함해서 반환
	}

	@GetMapping("/")
	public List<RoomVO> list(@RequestHeader("Authorization") String token) {
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		return roomDao.selectList(claimVO.getMemberId());
	}

	@DeleteMapping("/{roomNo}")
	public void delete(@PathVariable int roomNo) {
		roomDao.delete(roomNo);
	}

	@PostMapping("/enter")
	public void enter(@RequestBody RoomMemberDto roomMemberDto, @RequestHeader("Authorization") String token) {
		// 방이 없는 경우를 사전 차단
		RoomDto roomDto = roomDao.selectOne(roomMemberDto.getRoomNo());
		if (roomDto == null)
			throw new TargetNotFoundException("존재하지 않는 방");

		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		roomMemberDto.setMemberId(claimVO.getMemberId());// 아이디 설정
		roomDao.enter(roomMemberDto);// 등록
	}

	@PostMapping("/leave")
	public void leave(@RequestBody RoomMemberDto roomMemberDto, @RequestHeader("Authorization") String token) {
		// 방이 없는 경우를 사전 차단
		RoomDto roomDto = roomDao.selectOne(roomMemberDto.getRoomNo());
		if (roomDto == null)
			throw new TargetNotFoundException("존재하지 않는 방");

		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		roomMemberDto.setMemberId(claimVO.getMemberId());// 아이디 설정
		roomDao.leave(roomMemberDto);// 삭제
	}

	@GetMapping("/check/{roomNo}")
	public boolean check(@PathVariable int roomNo, @RequestHeader("Authorization") String token) {
		// 토큰 해석
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));

		// DB 검사
		RoomMemberDto roomMemberDto = new RoomMemberDto();
		roomMemberDto.setMemberId(claimVO.getMemberId());
		roomMemberDto.setRoomNo(roomNo);
		boolean canEnter = roomDao.check(roomMemberDto);

		return canEnter;
	}
}