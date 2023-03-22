package com.iwi.iwms.config.security.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.common.errors.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizationAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    	
    	log.error("Forbidden!!! message : " + e.getMessage());
    	
    	ErrorCode code = ErrorCode.AUTHORIZATION_FAILED;
    	
    	String er = ErrorResponse.builder()
	    		.code(code)
	    		.build()
	    		.toJson();
		
        response.setStatus(code.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(er);
    }
}