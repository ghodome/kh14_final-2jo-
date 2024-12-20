package com.art.vo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AuctionRequestVO {
	private int auctionScheduleNo;
	private int workNo;
	private String auctionSuccessBidder;
	private int auctionLot;
	private int auctionLowPrice;
	private int auctionHighPrice;
	private Timestamp auctionStartDate;
	private Timestamp auctionEndDate;
	private String auctionState;
	private int auctionHammerPrice;//낙찰가
	private int auctionBidCnt;//응찰횟수
	private String auctionConsigner;//위탁자
	private int auctionConsignmentFee;//위탁수수료
	private int auctionNetProceeds;//위탁대금 (최종 낙찰 가격 )- (위탁 수수료 )- (기타 비용)
	private int auctionBidIncrement;//호가단위
	private int auctionStartPrice;
	private int auctionBidPrice;
}