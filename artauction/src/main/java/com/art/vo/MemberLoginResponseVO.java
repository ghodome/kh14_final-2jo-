package com.art.vo;

import lombok.Data;

@Data
public class MemberLoginResponseVO {
	private String memberId;//로그인 성공한 사용자의 아이디
	private String memberLevel;//로그인 성공한 사용자의 등급
	private String accessToken;//나중에 들고올 토큰
	private String refreshToken;//어떤 이유로 액세스토큰이 사용이 불가해지면 갱신을 위한 토큰(아주 긴 시간 + DB에 저장)
}
