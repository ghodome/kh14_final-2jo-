package com.art.vo;

import lombok.Data;

@Data
public class ArtistListVO {
	private int artistNo;
	private String artistName;
	private String artistDescription;
	private String artistHistory;
	private String artistBirth;
	private String artistDeath;
	private Integer attachment;
}
