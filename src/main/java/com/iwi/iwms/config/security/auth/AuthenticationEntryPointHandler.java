package com.iwi.iwms.config.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,  HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
      
    	log.error("UnAuthorizaed!!! message : " + e.getMessage());
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(ErrorResponse.builder()
        		.request(request)
        		.status(HttpServletResponse.SC_UNAUTHORIZED)
        		.message("인증에 실패했습니다.")
        		.build()
        		.toJson());
    }
}