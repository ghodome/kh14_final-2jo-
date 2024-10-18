package com.art.service;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.dto.MemberDto;
import com.art.dto.MemberTokenDto;

@Service
public class MemberService {

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private EmailService emailService;

	public void findPw(String memberId, String memberEmail) {
	    MemberDto memberDto = sqlSession.selectOne("member.find", memberId);
	    
	    if (memberDto != null && memberDto.getMemberEmail().equals(memberEmail)) {
	        String token = UUID.randomUUID().toString();
	        MemberTokenDto memberToken = new MemberTokenDto();
	        memberToken.setTokenTarget(memberId);
	        memberToken.setTokenValue(token);
	        sqlSession.insert("member.saveToken", memberToken);

	        // Reset link 생성
	        String resetLink = "http://localhost:3000/#/changePw/" + token;

	        // 이메일 본문 설정
	        String body = "비밀번호 재설정 요청\n" +
	                      "아래 링크를 클릭하여 비밀번호를 재설정하세요:\n" +
	                      resetLink;

	        // 이메일 전송
	        emailService.sendEmail(memberEmail, "비밀번호 재설정", body);
	    } else {
	        throw new RuntimeException("아이디와 이메일이 일치하지 않습니다.");
	    }
	}


	public boolean resetPassword(String memberId, String newPassword, String token) {
		MemberTokenDto tokenDto = sqlSession.selectOne("member.findByToken", token);

		if (tokenDto != null && tokenDto.getTokenTarget().equals(memberId)) {
			MemberDto memberDto = sqlSession.selectOne("member.find", memberId);
			memberDto.setMemberPw(newPassword);
			sqlSession.update("member.save", memberDto);
			sqlSession.delete("member.deleteToken", token);
			return true;
		}
		return false;

	}

}
