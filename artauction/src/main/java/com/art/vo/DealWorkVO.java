package com.art.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class DealWorkVO {
	private int dealNo;
    private int bidNo;
    private String dealBuyer; 
    private Date dealTime;
    private int dealPrice;
    private String dealStatus;
    private String workTitle;
    private String artistName;
}
