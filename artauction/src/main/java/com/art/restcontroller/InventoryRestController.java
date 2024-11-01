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

import com.art.dao.InventoryDao;
import com.art.dto.InventoryDto;
import com.art.service.TokenService;
import com.art.vo.MemberClaimVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/inventory")
public class InventoryRestController {
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private TokenService tokenService;
	@PostMapping("/")
	public void insert(@RequestBody InventoryDto inventoryDto) {
		inventoryDao.insert(inventoryDto);
	}
	
	@GetMapping("/find")
	public List<InventoryDto> selectOne(@RequestHeader("Authorization")String token) {
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		return inventoryDao.selectList(claimVO.getMemberId());
	}
	@DeleteMapping("/{inventoryId}")
	public void delete(@PathVariable int inventoryId) {
		inventoryDao.delete(inventoryId);
	}
}
