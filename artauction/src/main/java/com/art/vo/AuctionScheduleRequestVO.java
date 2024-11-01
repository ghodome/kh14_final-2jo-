package com.art.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AuctionScheduleRequestVO {
	private String auctionScheduleTitle;
	private LocalDateTime auctionScheduleStartDate;
	private LocalDateTime auctionScheduleEndDate;
	private String auctionScheduleState;
	private String auctionScheduleNotice;
	
	private List<MultipartFile> attachList;
}
