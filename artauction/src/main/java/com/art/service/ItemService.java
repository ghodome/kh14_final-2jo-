package com.art.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.art.dao.ItemsDao;
import com.art.dto.ItemsDto;

@Service
public class ItemService {
	
	@Autowired
	private ItemsDao itemsDao;
	
	
	public void updateChance() {
	    List<ItemsDto> items = itemsDao.list();
	    
	    // 총 아이템 갯수
	    int totalItems = items.size();
	    
	    // 총 확률
	    int totalChance = 100;
	    
	    // 당첨 아이템의 총 확률 계산
	    int chanceForWin = 0;
	    int winItemsCount = 0;
	    
	    for (ItemsDto item : items) {
	        if ("Y".equals(item.getIsWin())) {
	            chanceForWin += item.getChance();
	            winItemsCount++;
	        }
	    }

	    // 꽝의 아이템 개수 계산
	    int lossItemsCount = totalItems - winItemsCount;
	    
	    // 꽝의 확률을 계산하기 위한 변수
	    int chanceForLoss = totalChance - chanceForWin;

	    // 꽝 아이템 확률 업데이트
	    for (ItemsDto item : items) {
	        if ("N".equals(item.getIsWin())) {
	            // 꽝 아이템의 확률을 남은 확률로 설정
	            item.setChance(chanceForLoss / lossItemsCount);
	        }
	        // 데이터베이스에 업데이트
	        itemsDao.update(item);
	    }
	}

	//아이템 추가
	public void	addItem(ItemsDto itemsDto) {
		itemsDao.update(itemsDto);
		updateChance();
	}
	//게임
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
	public void deleteItem(int itemId) {
		itemsDao.delete(itemId);
		updateChance();
	}
	//확률 계산
	
}
