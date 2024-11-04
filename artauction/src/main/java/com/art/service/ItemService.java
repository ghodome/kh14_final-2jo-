package com.art.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.art.dao.InventoryDao;
import com.art.dao.ItemsDao;
import com.art.dao.MemberDao;
import com.art.dto.InventoryDto;
import com.art.dto.ItemsDto;
import com.art.dto.MemberDto;
import com.art.error.TargetNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
	@Autowired
	private ItemsDao itemsDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private InventoryDao inventoryDao;
	
	// 확률 업데이트 메서드
	public void updateChance() {
	    List<ItemsDto> items = itemsDao.list();
	    
	    // 총 확률 (100%)
	    double totalChance = 100d;    
	    // 당첨 아이템의 총 확률 계산
	    double chanceForWin = 0;    
	    // 당첨 아이템 확률 계산 및 업데이트
	    for (ItemsDto item : items) {
	        if ("Y".equals(item.getIsWin())) {
	        	item.setChance(calculation(item.getItemValue())); // 추정가를 기준으로 확률 계산
	        	itemsDao.update(item);
	            chanceForWin += item.getChance();
	        }
	    }
	    //꽝 확률
	    double chanceForLoss = totalChance - chanceForWin;
	    // 꽝 아이템 확률 업데이트
	    for (ItemsDto item : items) {
	        if ("N".equals(item.getIsWin())) {
	            item.setChance(chanceForLoss);
	        }
	      
	       
	        itemsDao.update(item);
	    }
	}

	// 아이템 추가 메서드
	public void addItem(ItemsDto itemsDto) {
		itemsDao.update(itemsDto);
		updateChance();
	}
	
	public ItemsDto getRandomItem() {
	    List<ItemsDto> items = itemsDao.list();
	    double totalChance = 0; // double형으로 수정

	    // 각 아이템의 총 확률 계산
	    for (ItemsDto item : items) {
	        totalChance += item.getChance();
	    }

	    // 랜덤 값 생성 (0.00 ~ 100.00)
	    Random random = new Random();
	    double randomValue = random.nextDouble() * 100; // 0.0 ~ 100.0

	    // 아이템 선택
	    double cumulativeChance = 0; // double형으로 수정
	    for (ItemsDto item : items) {
	        cumulativeChance += item.getChance();
	        if (randomValue < cumulativeChance) {
	            return item; // 선택된 아이템 반환
	        }
	    }

	    return null; 
	}

	// 아이템 삭제 메서드
	public void deleteItem(int itemId) {
		itemsDao.delete(itemId);
		updateChance();
	}

	// 확률 계산 메서드
	public double calculation(int price) {
		if(price <= 0) {
			throw new TargetNotFoundException("상품의 추정가는 0보다 커야 합니다");
		}
		// 소수점 둘째 자리까지 확률 계산
		return Math.round((10000.0 / price) * 10000) / 100.0;
	}
	//아이템 동시성 제어
	@Transactional
	public synchronized ItemsDto randomRun(MemberDto memberDto) {
		if(memberDto.getMemberPoint()<10000)return null;
		memberDto.setMemberPoint(memberDto.getMemberPoint()-10000);
		memberDao.pointUpdate(memberDto);
		
        ItemsDto itemsDto =  getRandomItem(); // 랜덤 아이템 반환
        if(itemsDto.getIsWin().equals("N"))return itemsDto;
        else {
        	//아이템 정보를 인벤토리에 보내고
        	InventoryDto inventoryDto = new InventoryDto();
        	inventoryDto.setMemberId(memberDto.getMemberId());
        	inventoryDto.setItemId(itemsDto.getItemId());
        	inventoryDto.setItemName(itemsDto.getItemName());
        	inventoryDto.setItemValue(itemsDto.getItemValue());
        	inventoryDao.insert(inventoryDto);
        	//프론트로 아이템 정보를 보낸후
        	return itemsDto;
        }
	}
}
