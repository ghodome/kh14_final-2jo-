package com.art.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.MemberDao;
import com.art.dao.MemberTokenDao;
import com.art.dto.MemberDto;
import com.art.dto.MemberTokenDto;
import com.art.error.TargetNotFoundException;
import com.art.service.MemberService;
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
	private MemberService memberService;
	
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
	@GetMapping("/detail/{memberId}")
	public MemberDto detail(@PathVariable String memberId) {
		MemberDto memberDto = memberDao.selectOne(memberId);
		if(memberDto == null) {
			throw new TargetNotFoundException(memberId);
		}
		return memberDto;
	}
	@PatchMapping("/update")
	public void update(@RequestBody MemberDto memberDto) {
		boolean result = memberDao.update(memberDto);
		if(result == false) {
			throw new TargetNotFoundException();
		}
	}
	@DeleteMapping("/delete/{memberId}")
	public void delete(@PathVariable String memberId,
			@RequestHeader("Authorization") String token) {
		boolean result = memberDao.delete(memberId);
		if(result == false) {
			throw new TargetNotFoundException("찾을수없는 아이디입니다");
			 
		}
	}
	
	
	
	@PostMapping("/findPw")
	public String findPw(@RequestBody MemberDto memberDto) {
		memberService.findPw(memberDto.getMemberId(), memberDto.getMemberEmail());
		return "비밀번호 재설정 이메일이 전송돼었습니다";
	}
	@PostMapping("/changePw")
	public String changePw(@RequestBody MemberDto memberDto, @RequestParam String token) {
	    boolean success = memberService.resetPassword(memberDto.getMemberId(), memberDto.getMemberEmail(), token);
	    return success ? "비밀번호가 성공적으로 재설정되었습니다." : "비밀번호 재설정에 실패했습니다.";
	}
	@PostMapping("/refresh")
	public MemberLoginResponseVO refresh(
			@RequestHeader("Authorization") String refreshToken) {
		if(refreshToken == null) 
			throw new TargetNotFoundException("토큰 없음");
		if(tokenService.isBearerToken(refreshToken) == false)
			throw new TargetNotFoundException("Bearer 토큰 아님");
		
		
		MemberClaimVO claimVO = 
				tokenService.check(tokenService.removeBearer(refreshToken));
		if(claimVO.getMemberId() == null)
			throw new TargetNotFoundException("아이디 없음");
		if(claimVO.getMemberRank() == null)
			throw new TargetNotFoundException("등급 없음");
		
		
		MemberTokenDto memberTokenDto = new MemberTokenDto();
		memberTokenDto.setTokenTarget(claimVO.getMemberId());
		memberTokenDto.setTokenValue(tokenService.removeBearer(refreshToken));
		MemberTokenDto resultDto = memberTokenDao.selectOne(memberTokenDto);
		if(resultDto == null)
			throw new TargetNotFoundException("발급 내역이 없음");
		
		
		memberTokenDao.delete(memberTokenDto);
		
		
		MemberLoginResponseVO response = new MemberLoginResponseVO();
		response.setMemberId(claimVO.getMemberId());
		response.setMemberLevel(claimVO.getMemberRank());
		response.setAccessToken(tokenService.createAccessToken(claimVO));
		response.setRefreshToken(tokenService.createRefreshToken(claimVO));
		return response;
	}
	
	@GetMapping("/find")
	public MemberDto find(@RequestHeader("Authorization") String accessToken) {
		if(tokenService.isBearerToken(accessToken) == false)
			throw new TargetNotFoundException("유효하지 않은 토큰");
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
		
		MemberDto memberDto = memberDao.selectOne(claimVO.getMemberId());
		if(memberDto == null)
			throw new TargetNotFoundException("존재하지 않는 회원");
		
		memberDto.setMemberPw(null);
		
		return memberDto;
	}
}
