package com.art.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.AuctionDao;
import com.art.dao.InventoryDao;
import com.art.dao.ItemsDao;
import com.art.dao.MemberDao;
import com.art.dao.workDao;
import com.art.dto.AuctionDto;
import com.art.dto.InventoryDto;
import com.art.dto.ItemsDto;
import com.art.dto.MemberDto;
import com.art.dto.WorkDto;
import com.art.error.TargetNotFoundException;
import com.art.service.ItemService;
import com.art.service.TokenService;
import com.art.vo.ItemAcutionVO;
import com.art.vo.MemberClaimVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemsRestController {
	
	@Autowired
	private ItemsDao itemsDao;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private AuctionDao auctionDao;
	@Autowired
	private workDao workDao;
	@Autowired
	private InventoryDao inventoryDao;
	//상품 조회
	@GetMapping("/list")
	public List<ItemsDto> list(){
		itemService.updateChance();
		return itemsDao.rarityList();
	}
	//아이템뽑기
	@Transactional
	@GetMapping("/randomBox")
    public ItemsDto openRandomBox(
    		@RequestHeader("Authorization")String token) {
		MemberClaimVO claimVO = tokenService.check(tokenService.removeBearer(token));
		MemberDto memberDto = memberDao.selectOne(claimVO.getMemberId());
		if(memberDto.getMemberPoint()<10000)return null;
		memberDto.setMemberPoint(memberDto.getMemberPoint()-10000);
		memberDao.pointUpdate(memberDto);
		
        ItemsDto itemsDto =  itemService.getRandomItem(); // 랜덤 아이템 반환
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
	@DeleteMapping("/{itemId}")
	public void deleteItem(
			@PathVariable("itemId")int itemId
			) {
		itemService.deleteItem(itemId);
	}
	//등록
	@PostMapping("/")
	public void insert(@RequestBody ItemAcutionVO itemAuctionVO) {
		ItemsDto itemsDto = new ItemsDto();
		itemsDto.setAuctionNo(itemAuctionVO.getAuctionNo());
		itemsDto.setIsWin("Y");
		AuctionDto auctionDto = auctionDao.selectOne(itemsDto.getAuctionNo());
		
		int price = (auctionDto.getAuctionHighPrice()+auctionDto.getAuctionLowPrice())/2;
		if(price<=0)throw new TargetNotFoundException("가격 측정 x");
		WorkDto workDto = workDao.selectOne(auctionDto.getWorkNo());
		
		itemsDto.setItemName(workDto.getWorkTitle());
		itemsDto.setItemValue(price);
		
		itemsDao.insert(itemsDto);
		itemService.updateChance();
	}
}
