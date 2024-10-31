package com.art.dto;

import java.sql.Date;


import lombok.Data;

@Data
public class DealDto {
	private int dealNo;

    private int bidNo;
    private String dealBuyer; 
    private Date dealTime;
    private int dealPrice;
    private String dealStatus;

}
