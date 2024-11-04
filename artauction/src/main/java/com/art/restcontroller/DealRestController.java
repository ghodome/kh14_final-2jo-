package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.DealDao;
import com.art.dto.DealDto;
import com.art.service.TokenService;
import com.art.vo.DealWorkVO;
import com.art.vo.MemberClaimVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins={"http://localhost:3000"})
@RequestMapping("/deal")
public class DealRestController {
	@Autowired
	private DealDao dealDao;
	@Autowired
	private TokenService tokenService;
	@GetMapping("/")
	public List<DealWorkVO> list(
			@RequestHeader("Authorization")String token){
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		return dealDao.list(claimVO.getMemberId());
	}
	@PostMapping("/giveup/{dealNo}")
	public void giveup(@PathVariable int dealNo) {
		dealDao.updateStatus(dealNo);
	}
	@GetMapping("/list")
	public List<DealWorkVO> dealList(){
		return dealDao.dealList();
	}
}
