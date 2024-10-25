package com.art.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WorkInsertRequestVO {
	private int artistNo;
	private String workTitle;
	private String workDescription;
	private String workMaterials;
	private String workSize;
	private String workCategory;
	
	private List<MultipartFile> attachList;
}
