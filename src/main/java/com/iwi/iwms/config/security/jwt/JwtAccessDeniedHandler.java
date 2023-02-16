package com.iwi.iwms.config.security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    	
    	log.error("Forbidden!!! message : " + e.getMessage());
    	 
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(ErrorResponse.builder()
        		.request(request)
        		.status(HttpServletResponse.SC_FORBIDDEN)
        		.message("API 호출 권한이 없습니다.")
        		.build()
        		.toJson());
    }
}