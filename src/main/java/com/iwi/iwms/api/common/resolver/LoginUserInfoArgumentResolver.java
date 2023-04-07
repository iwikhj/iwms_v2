package com.iwi.iwms.api.common.resolver;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.config.redis.RedisProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserInfoArgumentResolver implements HandlerMethodArgumentResolver {
	
    private final RedisProvider redisProvider;
    
    private final ObjectMapper objectMapper;
    
	//private final UserService userService;
	
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
			.orElseThrow(() -> new CommonException(ErrorCode.AUTHENTICATION_FAILED, "로그인이 필요한 요청입니다."));
	
		String ssoKey = authentication.getName();
		
		if(request.getRequestURI().indexOf("logout") != -1) {
			redisProvider.delete(ssoKey);
			return null;
		}
		
		return Optional.ofNullable(objectMapper.convertValue(redisProvider.getHash(ssoKey, "user"), LoginUserInfo.class))
					.orElseThrow(() -> new CommonException(ErrorCode.AUTHENTICATION_FAILED, "로그인이 필요한 요청입니다."));
		
		//LoginUserInfo loginUserInfo = objectMapper.convertValue(redisProvider.getHash(ssoKey, "user"), LoginUserInfo.class);
		//return loginUserInfo == null ? userService.getLoginUser(ssoKey) : loginUserInfo;
	}

}
