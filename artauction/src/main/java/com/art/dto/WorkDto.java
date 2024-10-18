package com.art.dto;

import lombok.Data;

@Data
public class WorkDto {
	private int workNo;
	private String workTitle;
	private String workDescription;
	private String workMaterials;
	private String workSize;
	private String workCategory;
}
