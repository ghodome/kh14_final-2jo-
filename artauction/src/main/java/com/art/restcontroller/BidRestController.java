package com.art.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.BidDao;
import com.art.dto.BidDto;
import com.art.vo.AuctionContentVO;
import com.art.vo.WebsocketBidResponseVO;

@RestController
@CrossOrigin
@RequestMapping("/bid")
public class BidRestController {
	
	@Autowired
	private BidDao bidDao;
	
	@GetMapping("/bidMessageList/{auctionNo}")
	public List<WebsocketBidResponseVO> bidMessageList(@PathVariable int auctionNo){
		List<BidDto> list=bidDao.getBidListByAuctionNo(auctionNo);
		List<WebsocketBidResponseVO> response=new ArrayList<WebsocketBidResponseVO>();
		for(int i=0; i<list.size(); i++) {
			WebsocketBidResponseVO vo=new WebsocketBidResponseVO();
			vo.setTime(list.get(i).getBidTime());
			vo.setSenderMemberId(list.get(i).getMemberId());
			AuctionContentVO content=new AuctionContentVO();
			content.setContentForLot(list.get(i).getBidContent());
			content.setContentForSchedule(list.get(i).getBidContent());
			vo.setContent(content);
			response.add(vo);
		}
		return response;
	}
}
