package com.iwi.iwms.api.common.resolver;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.config.redis.RedisProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserInfoArgumentResolver implements HandlerMethodArgumentResolver {
	
    private final RedisProvider redis;
    
    private final ObjectMapper objectMapper;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUserInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		//String userId = (String)((ServletWebRequest) webRequest).getRequest().getAttribute("u");

		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Authentication authentication = Optional.ofNullable((Authentication) webRequest.getUserPrincipal())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."));
		
		String ssoKey = authentication.getName();
		
		if(request.getRequestURI().indexOf("logout") != -1) {
			redis.delete(ssoKey);
			return null;
		}
		
		/*
		LoginUserInfo loginUserInfo = objectMapper.convertValue(redis.getHash(ssoKey, "user"), LoginUserInfo.class);
		if(loginUserInfo == null) {
			loginUserInfo = userService.getLoginUser(ssoKey);
		}
		return loginUserInfo;
		*/
		
		return Optional.ofNullable(objectMapper.convertValue(redis.getHash(ssoKey, "user"), LoginUserInfo.class))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 정보를 찾을 수 없습니다."));
	}

}
