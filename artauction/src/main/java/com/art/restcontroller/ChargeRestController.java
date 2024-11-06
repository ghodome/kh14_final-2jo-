package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.ChargeDao;
import com.art.dto.ChargeDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/charge")
public class ChargeRestController {
	
	@Autowired
	private ChargeDao chargeDao;
	@Autowired
	private TokenService tokenService;
	
	@GetMapping("/")
	public List<ChargeDto> list(@RequestHeader("Authorization")String token){
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		return chargeDao.selectList(claimVO.getMemberId());
	}
}
