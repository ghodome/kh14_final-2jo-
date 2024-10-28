package com.art.vo;

import java.util.List;

import lombok.Data;

@Data
public class ArtistListResponseVO {
	private List<ArtistListVO> artistList;
	private boolean isLast;
	private int count;
}
