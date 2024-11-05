package com.art.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.art.interceptor.AdminInterceptor;
import com.art.interceptor.MemberInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer{

	@Autowired
	private MemberInterceptor memberInterceptor;
	
	@Autowired
	private AdminInterceptor adminInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberInterceptor)
					.addPathPatterns(
							"/member/**",
							"/item/**",
							"/payment/**",
							"/deal/"
							
							)
					.excludePathPatterns(
							"/member/join*",
							"/member/login*",
							"/member/findPw*",
							"/member/changePw",
							"/member/checkId",
							"/member/checkName",
							"/member/point"
							);
		registry.addInterceptor(adminInterceptor)
					.addPathPatterns(
							"/payment/giveup"	
							);
	}
}
