package com.art.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.MemberDao;
import com.art.dao.MemberTokenDao;
import com.art.dto.MemberDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;
import com.art.vo.MemberLoginRequestVO;
import com.art.vo.MemberLoginResponseVO;



@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberRestController {

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MemberTokenDao memberTokenDao;
	
	@PostMapping("/join")
	public void insert(@RequestBody MemberDto memberDto) {
		memberDao.insert(memberDto);
	}
	@PostMapping("/login")
	public MemberLoginResponseVO login(
			@RequestBody MemberLoginRequestVO vo) {
		MemberDto memberDto = memberDao.selectOne(vo.getMemberId());
		
//		boolean isValid = vo.getMemberPw().equals(memberDto.getMemberPw());
//		boolean isValid = encoder.matches(vo.getMemberPw(), memberDto.memberPw());
		
		
			MemberLoginResponseVO response = new MemberLoginResponseVO();
			//아이디, 등급, 액세스토큰
			response.setMemberId(memberDto.getMemberId());//아이디
			response.setMemberLevel(memberDto.getMemberRank());//등급
			MemberClaimVO claimVO = new MemberClaimVO();
			claimVO.setMemberId(memberDto.getMemberId());
			claimVO.setMemberRank(memberDto.getMemberRank());
			response.setAccessToken(tokenService.createAccessToken(claimVO));//액세스토큰
			response.setRefreshToken(tokenService.createRefreshToken(claimVO));//리프레시토큰
			return response;	
	}
}
