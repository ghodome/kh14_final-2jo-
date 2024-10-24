package com.art.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Service
@ConfigurationProperties(prefix = "custom.kakaopay")
public class KakaoPayProperties {
	private String secret;
	private String cid;
	//-는 카멜케이스로
	//secret-key = secretKey
}
