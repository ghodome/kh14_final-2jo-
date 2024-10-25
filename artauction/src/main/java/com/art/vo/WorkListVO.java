package com.art.vo;

import lombok.Data;

@Data
public class WorkListVO {
	private int workNo;
	private int artistNo;
	private String artistName;
	private String workTitle;
	private String workDescription;
	private String workMaterials;
	private String workSize;
	private String workCategory;
	private Integer attachment;
	
}
