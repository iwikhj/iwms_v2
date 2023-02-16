package com.iwi.iwms.config.web;

import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.iwi.iwms.api.common.resolver.LoginUserInfoArgumentResolver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private static final String TIMEZONE = "Asia/Seoul";
	
	private final LoginUserInfoArgumentResolver loginUserInfoArgumentResolver;
	
	@PostConstruct
	public void timezone() {
		TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE));
	}
	
    public void addViewControllers(ViewControllerRegistry registry) {
    	//registry.addViewController("/login").setViewName("login");
    }
	
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserInfoArgumentResolver);
    }

}

