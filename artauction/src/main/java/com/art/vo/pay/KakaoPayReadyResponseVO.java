package com.art.vo.pay;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

//카카오페이 결제 준비 응답 데이터
@Data
//snake case 를 camel case 로 변환해줘
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//적힌 필드 외에 모르는 항목이 있으면 넘어가세요!
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPayReadyResponseVO {
	private String tid;
//	private String nextRedirectAppUrl;
//	private String nextRedirectMobileUrl;
	private String nextRedirectPcUrl;
//	private String androidAppScheme;
//	private String iosAppScheme;
	private LocalDateTime createdAt;
}
