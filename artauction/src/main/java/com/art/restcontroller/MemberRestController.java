package com.art.restcontroller;



import java.io.Console;
import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

import com.art.dao.ChargeDao;
import com.art.dao.MemberDao;
import com.art.dao.MemberTokenDao;
import com.art.dto.ChargeDto;
import com.art.dto.MemberDto;
import com.art.dto.MemberTokenDto;
import com.art.error.TargetNotFoundException;
import com.art.service.KakaoPayService;
import com.art.service.MemberService;
import com.art.service.TokenService;
import com.art.vo.MemberApproveRequestVO;
import com.art.vo.MemberClaimVO;
import com.art.vo.MemberComplexRequestVO;
import com.art.vo.MemberComplexResponseVO;
import com.art.vo.MemberLoginRequestVO;
import com.art.vo.MemberLoginResponseVO;
import com.art.vo.MemberPurchaseRequestVO;
import com.art.vo.pay.KakaoPayApproveRequestVO;
import com.art.vo.pay.KakaoPayApproveResponseVO;
import com.art.vo.pay.KakaoPayReadyRequestVO;
import com.art.vo.pay.KakaoPayReadyResponseVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
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
	@Autowired
	private ChargeDao chargeDao;
	
	@Autowired
	private KakaoPayService kakaoPayService;
	@PostMapping("/join")
	public void insert(@RequestBody MemberDto memberDto) {
		memberDao.insert(memberDto);
	}
	@PostMapping("search")
	public MemberComplexResponseVO search(
			@RequestBody MemberComplexRequestVO vo) {
		int count = memberDao.searchCount(vo);
		boolean last = vo.getEndRow() == null || count <= vo.getEndRow();
		
		MemberComplexResponseVO response = new MemberComplexResponseVO();
		response.setMemberList(memberDao.search(vo));
		response.setCount(count);
		response.setLast(last);
		return response;
	}
	
	
	@PostMapping("/login")
	public MemberLoginResponseVO login(
			@RequestBody MemberLoginRequestVO vo) {
		MemberDto memberDto = memberDao.selectOne(vo.getMemberId());
		if(memberDto == null) {
			throw new TargetNotFoundException("아이디 없음");
		}
		
		
		boolean isValid = vo.getMemberPw().equals(memberDto.getMemberPw());
//		boolean isValid = encoder.matches(vo.getMemberPw(), memberDto.memberPw());
		
		if(isValid) {			
			MemberLoginResponseVO response = new MemberLoginResponseVO();
			//아이디, 등급, 액세스토큰
			response.setMemberId(memberDto.getMemberId());//아이디
			response.setMemberRank(memberDto.getMemberRank());//등급
			MemberClaimVO claimVO = new MemberClaimVO();
			claimVO.setMemberId(memberDto.getMemberId());
			claimVO.setMemberRank(memberDto.getMemberRank());
			response.setAccessToken(tokenService.createAccessToken(claimVO));//액세스토큰
			response.setRefreshToken(tokenService.createRefreshToken(claimVO));//리프레시토큰
			return response;	
		}
		else {
			throw new TargetNotFoundException("비밀번호 불일치");
		}
	}
	@GetMapping("/{memberId}")
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
	//- Refresh Token으로 로그인 하는 기능
		//- 보안이 매우 취약한 기능이므로 보안을 올리기 위해 각종 장치를 추가
		//- DB검증 등...
		//- Authorization이라는 헤더에 있는 값을 읽어서 검사한 뒤 갱신 처리
		//- 검증할 수 없는 토큰 또는 기타 오류 발생 시 404 처리
		@PostMapping("/refresh")
		public MemberLoginResponseVO refresh(
				@RequestHeader("Authorization") String refreshToken) {
			//[1] refreshToken이 없거나 Bearer로 시작하지 않으면 안됨
			if(refreshToken == null) 
				throw new TargetNotFoundException("토큰 없음");
			if(tokenService.isBearerToken(refreshToken) == false)
				throw new TargetNotFoundException("Bearer 토큰 아님");
			
			//[2] 토큰에서 정보를 추출
			MemberClaimVO claimVO = 
					tokenService.check(tokenService.removeBearer(refreshToken));
			if(claimVO.getMemberId() == null)
				throw new TargetNotFoundException("아이디 없음");
			if(claimVO.getMemberRank() == null)
				throw new TargetNotFoundException("등급 없음");
			
			//[3] 토큰 발급 내역을 조회
			MemberTokenDto memberTokenDto = new MemberTokenDto();
			memberTokenDto.setTokenTarget(claimVO.getMemberId());
			memberTokenDto.setTokenValue(tokenService.removeBearer(refreshToken));
			MemberTokenDto resultDto = memberTokenDao.selectOne(memberTokenDto);
			if(resultDto == null)//발급내역이 없음 
				throw new TargetNotFoundException("발급 내역이 없음");
			
			//[4] 기존의 리프시 토큰 삭제
			memberTokenDao.delete(memberTokenDto);
			
			//[5] 로그인 정보 재발급
			MemberLoginResponseVO response = new MemberLoginResponseVO();
			response.setMemberId(claimVO.getMemberId());
			response.setMemberRank(claimVO.getMemberRank());
			response.setAccessToken(tokenService.createAccessToken(claimVO));//재발급
			response.setRefreshToken(tokenService.createRefreshToken(claimVO));//재발급
			return response;
		}

	

		//회원 본인의 정보를 반환하는 기능
		//- 아이디를 변경할 수 없도록 Authorization 헤더에서 정보를 읽어 조회한 뒤 반환
		@GetMapping("/find")
		public MemberDto find(@RequestHeader("Authorization") String accessToken) {
			if(tokenService.isBearerToken(accessToken) == false) 
				throw new TargetNotFoundException("유효하지 않은 토큰");
			MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(accessToken));
			
			MemberDto memberDto = memberDao.selectOne(claimVO.getMemberId());
			if(memberDto == null)
				throw new TargetNotFoundException("존재하지 않는 회원");
			
			memberDto.setMemberPw(null);//비밀번호 제거
			
			return memberDto;
		}
	//충전
		@PostMapping("/charge/purchase")
		public KakaoPayReadyResponseVO purchase(
				@RequestBody MemberPurchaseRequestVO request
//				,@RequestHeader("Authorization")String token 토큰생기면 이거 다시 바꿔야됨
				) throws URISyntaxException {
			//카카오페이에 보낼 최종 결제 정보를 생성
			//결제 준비 요청을 보내고
			//사용자에게 필요한 정보를 전달
			MemberClaimVO claimVO = tokenService.check("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzIyNjI0ODAsImlzcyI6IktIYWNhZGVteSIsImlhdCI6MTcyOTU4NDA4MCwibWVtYmVySWQiOiJ0ZXN0dXNlcjEiLCJtZW1iZXJSYW5rIjoi7ZqM7JuQIn0.1xsMvbxvjMMSLq239HtLBBj3CIxmi-J-MLtQ0obkEm0");
			
			KakaoPayReadyRequestVO requestVO = new KakaoPayReadyRequestVO();
			requestVO.setPartnerOrderId(UUID.randomUUID().toString());
			requestVO.setPartnerUserId(claimVO.getMemberId());
			requestVO.setItemName("K옥션 포인트 충전");
			requestVO.setTotalAmount(request.getTotalAmount());
			requestVO.setApprovalUrl(request.getApprovalUrl());
			requestVO.setCancelUrl(request.getCancelUrl());
			requestVO.setFailUrl(request.getFailUrl());
			KakaoPayReadyResponseVO responseVO = kakaoPayService.ready(requestVO);
			
			log.info("리스폰스브이오 : {}",responseVO);
			return responseVO;
		}
		@Transactional
		@PostMapping("/charge/approve")
		public KakaoPayApproveResponseVO approve(
//				@RequestHeader("Authorization")String token,
				@RequestBody MemberApproveRequestVO request) throws URISyntaxException {
			KakaoPayApproveRequestVO requestVO = new KakaoPayApproveRequestVO();
			MemberClaimVO claimVO = tokenService.check("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzIyNjI0ODAsImlzcyI6IktIYWNhZGVteSIsImlhdCI6MTcyOTU4NDA4MCwibWVtYmVySWQiOiJ0ZXN0dXNlcjEiLCJtZW1iZXJSYW5rIjoi7ZqM7JuQIn0.1xsMvbxvjMMSLq239HtLBBj3CIxmi-J-MLtQ0obkEm0");
			requestVO.setPartnerOrderId(request.getPartnerOrderId());
			requestVO.setPartnerUserId(claimVO.getMemberId());
			requestVO.setTid(request.getTid());
			requestVO.setPgToken(request.getPgToken());
			KakaoPayApproveResponseVO responseVO = kakaoPayService.approve(requestVO);
			//최종 결제가 완료된 시점에 DB에 결제에 대한 기록을 남긴다
			
			//[1] 대표 정보 등록
			int chargeSeq = chargeDao.chargeSequence();
			ChargeDto chargeDto = new ChargeDto();
			chargeDto.setChargeNo(chargeSeq);//결제번호
			chargeDto.setChargeTid(responseVO.getTid());//거래번호
			chargeDto.setChargeName(responseVO.getItemName());//거래상품명
			chargeDto.setChargeTotal(responseVO.getAmount().getTotal());//거래금액
			chargeDto.setChargeRemain(chargeDto.getChargeTotal());//취소가능금액
			chargeDto.setMemberId(claimVO.getMemberId());//구매자ID
			chargeDao.chargeInsert(chargeDto);
			MemberDto memberDto = memberDao.selectOne(chargeDto.getMemberId());
			memberDto.setMemberPoint(memberDto.getMemberPoint()+chargeDto.getChargeTotal());
			memberDao.pointUpdate(memberDto);
//				BookDto bookDto = bookDao.selectOne(qtyVO.getBookId());
//				if(bookDto==null)throw new TargetNotFoundException("존재하지 않는 도서");
//				int paymentDetailSql = paymentDao.paymentDetailSequence();
//				PaymentDetailDto paymentDetailDto =new PaymentDetailDto();
//				paymentDetailDto.setPaymentDetailNo(paymentDetailSql);//번호 설정
//				paymentDetailDto.setPaymentDetailName(bookDto.getBookTitle());//상품명
//				paymentDetailDto.setPaymentDetailPrice(bookDto.getBookPrice());//상품판매
//				paymentDetailDto.setPaymentDetailItem(bookDto.getBookId());//상품번호
//				paymentDetailDto.setPaymentDetailQty(qtyVO.getQty());//구매수량
//				paymentDetailDto.setPaymentDetailOrigin(paymentSeq);//결제대표번호
//				paymentDao.paymentDetailInsert(paymentDetailDto);
			
			return responseVO;

		}
}
