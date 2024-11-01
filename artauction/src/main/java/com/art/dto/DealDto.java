package com.art.dto;

import java.util.Date;


import lombok.Data;

@Data
public class DealDto {
	private int dealNo;

    private int bidNo;
    private String dealBuyer; 
    private Date dealTime;
    private int dealPrice;
    private String dealStatus;
    private Date dealCancelTime;

}
