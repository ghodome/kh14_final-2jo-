package com.art.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiInfo() {
		//API 문서의 대표 정보 설정
//		Info info = new Info();
//		info.setVersion("v0.0.1");
//		info.setTitle("내가 만든 REST API");//제목
//		info.setDescription("React에서 이용하기 위해 만든 백엔드 문서");
		
		// builder 패턴을 사용한 객체 생성
		Info info = new Info().version("v0.0.1").title("내가 만든 REST API")
				.description("React에서 이용하기 위해 만든 백엔드 문서");
		
/*
		(+추가)
		- jWT 인증방식으로 변경했기 때문에 해당 기능들을 문서에서 사용할 수 없다
		- 이를 해결하기 위해서 문서에도 jwt 인증버튼을 추가할 예정
*/		
		String jwtName="Authorization";
		SecurityRequirement requirement=new SecurityRequirement();
		requirement.addList(jwtName);
		
		Components components = new Components()
				.addSecuritySchemes(jwtName, new SecurityScheme()
						.name(jwtName)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						);
		
		return new OpenAPI().info(info)
				.addSecurityItem(requirement)
				.components(components);
	}
}
