package com.art.vo;

import java.util.List;

import lombok.Data;
@Data
public class WorkListResponseVO {
	private List<WorkListVO> workList;
	private boolean isLast;
	private int count;
}
