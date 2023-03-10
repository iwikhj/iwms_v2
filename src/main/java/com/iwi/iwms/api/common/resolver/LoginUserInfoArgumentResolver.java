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
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.config.redis.RedisProvider;
import com.iwi.iwms.utils.CookieUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserInfoArgumentResolver implements HandlerMethodArgumentResolver {

	private final UserService userService;
	
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
		Authentication authentication = (Authentication) webRequest.getUserPrincipal();
		String ssoId = authentication.getName();
		
		//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		//authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_IWMS_USER"));

		//redis: ????????? ????????? ?????? ???????????? 
		LoginUserInfo loginUserInfo = objectMapper.convertValue(redis.getHash(ssoId, "user"), LoginUserInfo.class);
        log.info("============== User info in Redis: {}", loginUserInfo == null ? "Not found user <null>" : loginUserInfo);
        
		if(loginUserInfo == null) {
			loginUserInfo = userService.getLoginUser(ssoId);
		}
		
		//db?????? ?????? ?????? ????????? ?????? ?????? ????????? ??????
		//return Optional.ofNullable(objectMapper.convertValue(redis.getHash(ssoId, "user"), LoginUserInfo.class))
		//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "?????? ????????? ??????????????? ??????????????????"));
		
		//request.setAttribute("user", loginUserInfo);
		
		return loginUserInfo;
	}

}
