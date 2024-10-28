package com.art.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ArtistInsertRequestVO {
	private String artistName;
	private String artistDescription;
	private String artistHistory;
	private String artistBirth;
	private String artistDeath;
	
	private List<MultipartFile> attachList;
}
