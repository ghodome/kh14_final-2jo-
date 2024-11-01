package com.art.vo;

import com.art.advice.JsonEmptyStringToNullDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class AuctionListRequestVO {
	@JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
    private String column, keyword;
    @JsonDeserialize(using = JsonEmptyStringToNullDeserializer.class)
    private Integer beginRow, endRow;
}
