package com.iwi.iwms.config.springdoc;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {
	
	@Value("${app.version}") 
	private String version;
	
    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
            .title("openapi docs")
            .version(version)
            .description("잘못된 부분이나 오류 발생 시 <a href='#' title='khj@iwi.co.kr'>담당 개발자</a>에게 문의 바랍니다.");

	    // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme() 
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION);
	
    	// Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
            // Security 인증 컴포넌트 설정
            .components(new Components().addSecuritySchemes("JWT", bearerAuth))
            // API 마다 Security 인증 컴포넌트 설정
            .addSecurityItem(addSecurityItem)
            .info(info);
    }
    
    @Bean
    public OpenApiCustomiser sortTagsAlphabetically() {
        return openApi -> openApi.setTags(openApi.getTags()
                .stream()
                .sorted(Comparator.comparing(tag -> StringUtils.stripAccents(tag.getName())))
                .collect(Collectors.toList()));
    }
}
