package com.art.vo;

import java.util.List;

import com.art.dto.ChargeDetailDto;
import com.art.dto.ChargeDto;

import lombok.Data;

@Data 
public class ChargeTotalVO {
	private ChargeDto chargeDto;
	private List<ChargeDetailDto> chargeDetailList;
}
