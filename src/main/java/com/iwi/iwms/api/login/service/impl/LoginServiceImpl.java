package com.iwi.iwms.api.login.service.impl;

import java.time.Duration;
import java.util.Optional;

import javax.ws.rs.NotAuthorizedException;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.redis.RedisConnectionFailureException;
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
import com.iwi.iwms.api.user.mapper.UserMapper;
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

	private final UserMapper userMapper;
	
	private final AuthProvider authProvider;
	
    private final RedisProvider redis;
    
    private final ObjectMapper objectMapper;
    
	@Override
	public AccessTokenResponse login(Login login) {
		// 등록된 ID 확인
		UserInfo userInfo = Optional.ofNullable(userMapper.getUserById(login.getUsername()))
				.orElseThrow(() -> new CommonException(ErrorCode.LOGIN_FAILED_INCORRECT_ID_PW));				
		
		// 사용불가 ID
		if(userInfo.getUseYn().equals("N")) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_SUSPENDED_ID);
		}
		
		// 비밀번호 5회 이상 불일치
		if(userInfo.getLoginErrCnt() >= 5) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_RETRY_EXCEEDED);
		}
		
		try {
			// 인증 서버 인증 요청 
			AccessTokenResponse accessTokenResponse = authProvider.grantToken(login.getUsername(), login.getPassword());
			log.info("USERNAME: [{}]", login.getUsername());
			String key = userInfo.getSsoKey();
			
			// 로그인한 사용자의 정보를 Redis 서버에 저장한다. 이후 요청에 대한 사용자 정보는 Redis 서버에서 불러온다.
			// 사용자 정보의 만료 시간은 리플레시 토큰의 만료 시간과 동일하게 설정한다. 리플레시 토큰 만료시에는 다시 로그인해야 한다.
			LoginUserInfo loginUserInfo = userMapper.getLoginUser(key);
			if(redis.hasKey(key)) {
				redis.delete(key);
			}
			
			long timeout = Duration.ofSeconds(accessTokenResponse.getRefreshExpiresIn()).toMillis();
			redis.setHash(key, "user", loginUserInfo, timeout);
			redis.setHash(key, "refreshToken", accessTokenResponse.getRefreshToken(), timeout);
			
			// 로그인한 사용자의 접속IP를 저장 및 LOGIN_ERR_CNT 초기화. 
			userMapper.updateLoginSuccess(User.builder().loginIp(login.getLoginIp()).userSeq(userInfo.getUserSeq()).build());
			
			return accessTokenResponse;
		}  catch(RedisConnectionFailureException e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, e.getMessage());
		} catch(NotAuthorizedException e) {
			userMapper.updateLoginFailure(User.builder().userId(userInfo.getUserId()).build());
			throw new CommonException(ErrorCode.LOGIN_FAILED_INCORRECT_ID_PW);
		} catch(Exception e) {
			//e.printStackTrace();
			throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
	}
	
	@Override
	public ReissueResponse reissue(Reissue reissue) {
        IntrospectResponse introspect = authProvider.tokenIntrospect(reissue.getRefreshToken());
        log.info("[INTROSPECT RESULT] <{}>", introspect);
        
        if(introspect == null || !introspect.isActive()) {
			throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "리플레시 토큰 검증 실패.");
        }
		
        LoginUserInfo loginUserInfo = objectMapper.convertValue(redis.getHash(introspect.getSub(), "user"), LoginUserInfo.class);
        if(loginUserInfo == null || !loginUserInfo.getLoginIp().equals(reissue.getLoginIp())) {
        	throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "로그인 아이피와 요청 아이피가 일치하지 않음.");
        }
        
		String storedRefreshtoken = (String) redis.getHash(introspect.getSub(), "refreshToken");
		if(storedRefreshtoken == null || !storedRefreshtoken.equals(reissue.getRefreshToken())) {
        	throw new CommonException(ErrorCode.AUTHENTICATION_REISSUE_FAILED, "요청한 리프레시 토큰과 서버에 저장된 리프레시 토큰이 일치하지 않습니다.");
		}
    	return authProvider.reissue(reissue.getRefreshToken()).of();
	}

}
