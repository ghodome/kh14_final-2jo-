package com.art.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "http://localhost:8080/uploads/**" 경로를 "D:/upload/kh14fb" 디렉터리로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/upload/kh14fb/");  // 슬래시 주의
    }

}
