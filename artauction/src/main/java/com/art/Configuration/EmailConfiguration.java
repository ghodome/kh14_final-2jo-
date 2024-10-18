
package com.art.Configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {
	
	@Autowired
	private CustomEmailProperties customEmailProperties;

//	수동으로 객체를 등록하기 위해서는 @Bean 을 사용한다
	@Bean
	public JavaMailSender sender() {//김밥 (업캐스팅)
//	public JavaMailSenderImpl sender() {//참치김밥
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		
		sender.setHost(customEmailProperties.getHost());//이용할 업체의 호스트정보
		sender.setPort(customEmailProperties.getPort());//이용할 업체의 포트번호
		sender.setUsername(customEmailProperties.getUsername());//계정
		sender.setPassword(customEmailProperties.getPassword());//앱 비밀번호
		
		Properties props = new Properties();//자바에서 제공하는 문자열 Key=Value 저장소
		props.setProperty("mail.smtp.auth", "true");//인증 후 이용하도록 설정
		props.setProperty("mail.smtp.debug", "true");//디버깅 허용 설정
		props.setProperty("mail.smtp.starttls.enable", "true");//TLS 사용 설정
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");//TLS 버전 설정
		props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");//신뢰할 수 있는 주소로 등록
		sender.setJavaMailProperties(props);
		
		return sender;
	}
	
}
