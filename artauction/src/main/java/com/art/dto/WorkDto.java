package com.art.dto;

import lombok.Data;

@Data
public class WorkDto {
	private int workNo;
	private String workTitle;
	private String workDescription;
	private String materials;
	private int workSize;
	private String workCategory;
}
