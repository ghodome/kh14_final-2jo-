package com.art.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.dao.ItemsDao;
import com.art.dto.ItemsDto;
import com.art.error.TargetNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
	@Autowired
	private ItemsDao itemsDao;
	
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
	
	// 랜덤 아이템 반환 메서드 (게임 진행)
	public ItemsDto getRandomItem() {
	    List<ItemsDto> items = itemsDao.list();
	    int totalChance = 0;

	    // 각 아이템의 총 확률 계산
	    for (ItemsDto item : items) {
	        totalChance += item.getChance();
	    }

	    // 랜덤 값 생성
	    Random random = new Random();
	    int randomValue = random.nextInt(totalChance); // 0 ~ totalChance - 1

	    // 아이템 선택
	    int cumulativeChance = 0;
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
}
