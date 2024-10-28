package com.art.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.InventoryDao;
import com.art.dto.InventoryDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/inventory")
public class InventoryRestController {
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@PostMapping("/")
	public void insert(@RequestBody InventoryDto inventoryDto) {
		inventoryDao.insert(inventoryDto);
	}
	
	@GetMapping("/")
	public InventoryDto selectOne(@PathVariable String memberId) {
		return inventoryDao.selectOne(memberId);
	}
	@DeleteMapping("/{inventoryId}")
	public void delete(@PathVariable int inventoryId) {
		inventoryDao.delete(inventoryId);
	}
}
