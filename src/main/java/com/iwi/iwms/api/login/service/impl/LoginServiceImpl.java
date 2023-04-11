package com.iwi.iwms.api.login.service.impl;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.api.login.service.LoginService;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.config.redis.RedisProvider;
import com.iwi.iwms.config.security.auth.AuthProvider;
import com.iwi.iwms.config.security.auth.IntrospectResponse;
import com.iwi.iwms.config.security.auth.ReissueResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

	private final UserService userService;
	
	private final AuthProvider authProvider;
	
    private final RedisProvider redisProvider;
    
    private final ObjectMapper objectMapper;
    
	@Override
	public AccessTokenResponse login(Login login) {
		// 등록된 ID 확인
    	UserInfo userInfo = userService.getUserById(login.getUsername());
		if(userInfo == null) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_INCORRECT_ID_PW);
		}
		
		// 사용불가 ID
		if(userInfo.getUseYn().equals("N")) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_SUSPENDED_ID);
		}
		
		// 비밀번호 5회 이상 불일치
		if(userInfo.getLoginErrCnt() >= 5) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_RETRY_EXCEEDED);
		}
		
		// 인증 서버 인증 요청
		AccessTokenResponse accessTokenResponse = null;
		try {
			accessTokenResponse = authProvider.grantToken(login.getUsername(), login.getPassword());
		} catch(CommonException e) {
			if(ErrorCode.LOGIN_FAILED_INCORRECT_ID_PW == e.getCode()) {
				// 패스워드 불일치로 로그인 실패. LOGIN_ERR_CNT 증가
				userService.updateLoginFailure(User.builder()
						.userId(userInfo.getUserId())
						.build());	
			}
			throw new CommonException(e.getCode(), e.getReason());
		}
		
		String key = userInfo.getSsoKey();
		log.info("USERNAME: [{}], KEY: [{}]", login.getUsername(), key);
		
		// 사용자의 접속 정보 저장 및 LOGIN_ERR_CNT 초기화
		userService.updateLoginSuccess(User.builder()
				.loginIp(login.getLoginIp())
				.userSeq(userInfo.getUserSeq())
				.build());	
		
		// 로그인 사용자 정보 불러오기
		LoginUserInfo loginUserInfo = userService.getLoginUser(key);
		
		// Redis에 key가 존재하는지 확인하고 있으면 삭제
		if(redisProvider.hasKey(key)) {
			redisProvider.delete(key);
		}
		
		// 로그인 사용자 정보 Redis 등록
		redisProvider.setHash(key, "user", loginUserInfo);
		redisProvider.setHash(key, "refreshToken", accessTokenResponse.getRefreshToken());
		
		// TTL 설정. 리플레시 토큰의 만료시간과 동일하게 설정(초)
		redisProvider.setTtl(key, accessTokenResponse.getRefreshExpiresIn());
		
		return accessTokenResponse;
	}
	
	@Override
	public ReissueResponse reissue(Reissue reissue) {
        IntrospectResponse introspect = authProvider.tokenIntrospect(reissue.getRefreshToken());
        log.info("[INTROSPECT RESULT] <{}>", introspect);
        
        if(introspect == null || !introspect.isActive()) {
			throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "리플레시 토큰 검증 실패.");
        }
        
        LoginUserInfo loginUserInfo = objectMapper.convertValue(redisProvider.getHash(introspect.getSub(), "user"), LoginUserInfo.class);
        if(loginUserInfo == null || loginUserInfo.getLoginIp() == null || reissue.getLoginIp() == null || !loginUserInfo.getLoginIp().equals(reissue.getLoginIp())) {
        	throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "로그인 아이피와 요청 아이피가 일치하지 않습니다.");
        }
        
		String storedRefreshtoken = (String) redisProvider.getHash(introspect.getSub(), "refreshToken");
		if(storedRefreshtoken == null || !storedRefreshtoken.equals(reissue.getRefreshToken())) {
        	throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "요청한 리프레시 토큰과 서버에 저장된 리프레시 토큰이 일치하지 않습니다.");
		}
    	return authProvider.reissue(reissue.getRefreshToken()).of();
	}

}
