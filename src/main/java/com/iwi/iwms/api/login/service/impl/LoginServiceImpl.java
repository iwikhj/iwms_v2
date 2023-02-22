package com.iwi.iwms.api.login.service.impl;

import java.time.Duration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.login.service.LoginService;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.mapper.UserMapper;
import com.iwi.iwms.config.redis.RedisProvider;
import com.iwi.iwms.config.security.keycloak.KeycloakProvider;
import com.iwi.iwms.utils.CookieUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

	private final KeycloakProvider keycloakProvider;
	
	private final UserMapper userMapper;
	
    private final RedisProvider redis;
	
	@Override
	public AccessTokenResponse login(HttpServletRequest request, HttpServletResponse response, Login login) {
		
		// 등록된 Id인지 확인
		UserInfo userInfo = Optional.ofNullable(userMapper.findById(login.getUsername()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "등록되지 않은 사용자 또는 잘못된 비밀번호입니다."));
		
		try {
			// 인증 서버를 통한 인증 요청 
			AccessTokenResponse accessTokenResponse = keycloakProvider.grantToken(login.getUsername(), login.getPassword());
			String ssoId = userInfo.getSsoId();
			
			// 로그인한 사용자의 정보를 Redis 서버에 저장한다. 이후 요청에 대한 사용자 정보는 Redis 서버에서 불러온다.
			// 사용자 정보의 만료 시간은 리플레시 토큰의 만료 시간과 동일하게 설정한다. 리플레시 토큰 만료시에는 다시 로그인해야 한다.
			LoginUserInfo loginUserInfo = userMapper.findLoginUser(ssoId);
			
			if(redis.hasKey(ssoId)) {
				redis.delete(ssoId);
			}
			
			loginUserInfo.setRefreshToken(accessTokenResponse.getRefreshToken());
			
			long timeout = Duration.ofSeconds(accessTokenResponse.getRefreshExpiresIn()).toMillis();
			redis.setHash(ssoId, "user", loginUserInfo, timeout);
			
			// 액세스 토큰 및 리프레시 토큰을 응답 헤더에 담아서 클라이언트에 제공한다.
			response.setHeader("access_token", accessTokenResponse.getToken());
			response.setHeader("refresh_token", accessTokenResponse.getRefreshToken());
			
			// 리프레시 토큰은 쿠키에 담아서 저장한다.
			CookieUtil.setCookie(response, "refresh_token", accessTokenResponse.getRefreshToken(), false);

			// 로그인한 사용자의 접속IP를 저장 및 LOGIN_ERR_CNT 초기화. 
			String accessIp = request.getRemoteAddr().toString();
			userInfo.setLastLoginIp(accessIp);
			userMapper.updateLoginSuccess(userInfo.asUser());
			
			return accessTokenResponse;
		} catch(RedisConnectionFailureException e) {
			throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Unable to connect to Redis");
		} catch(Exception e) {
			userMapper.updateLoginFailure(userInfo.asUser());
			log.info("USERNAME: [{}], LOGIN ERROR COUNT: [{}]", userInfo.getUserId(), userInfo.getLoginErrCnt() + 1);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "등록되지 않은 사용자 또는 잘못된 비밀번호입니다.");
		}
		
	}

}
