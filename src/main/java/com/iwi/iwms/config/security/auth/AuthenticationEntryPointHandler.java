package com.iwi.iwms.config.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.common.errors.ErrorResponse;

@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,  HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    	ErrorCode code = ErrorCode.AUTHENTICATION_FAILED;
    	
    	String er = ErrorResponse.builder()
	    		.code(code)
	    		.message(e.getMessage())
	    		.build()
	    		.toJson();
		
        response.setStatus(code.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(er);
    }
}