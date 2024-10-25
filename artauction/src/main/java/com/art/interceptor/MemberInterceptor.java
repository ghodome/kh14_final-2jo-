package com.art.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberInterceptor implements HandlerInterceptor{
	
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//[1] OPTIONS 요청이 들어오면 통과시킨다
		//- options는 통신이 가능한지 확인하는 선발대 형식의 요청
		//- CORS 상황이거나 GET, HEAD, POST와 같은 일반적인 요청이 아니면 발생
		String method = request.getMethod();
		if(method.toLowerCase().equals("options")) {
			return true;
		}
		
		//[2] Authorization 헤더를 검사
		//(1) Authorization 헤더가 반드시 존재해야 함
		//(2) 헤더의 값은 Bearer로 시작해야 함
		//(3) 해석했을 때 memberId와 memberLevel이 나와야 함 (유효한 토큰)
		//(4) 이 중 하나라도 일치하지 않는다면 정상적인 로그인이 아니라고 간주 (401)
		try {
			String token = request.getHeader("Authorization");
			if(token == null) throw new Exception("헤더 없음");//(1)
			
			if(tokenService.isBearerToken(token) == false)
				throw new Exception("Bearer 토큰이 아님");//(2)
			
			String realToken = tokenService.removeBearer(token);
			
			MemberClaimVO claimVO = tokenService.check(realToken);//(3)
			log.info("아이디 = {}, 등급 = {}", claimVO.getMemberId(), claimVO.getMemberRank());
			return true;
		}
		catch(Exception e) {
			response.sendError(401);//(4)
			return false;
		}
	}
}
