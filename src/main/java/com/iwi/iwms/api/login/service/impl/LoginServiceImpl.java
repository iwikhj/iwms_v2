package com.iwi.iwms.api.login.service.impl;

import java.time.Duration;
import java.util.Optional;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
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
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "등록되지 않은 사용자 또는 잘못된 비밀번호입니다."));
				
		// 사용불가 ID
		if(userInfo.getUseYn().equals("N")) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용 정지된 아이디입니다.");
		}
		
		// 비밀번호 5회 이상 불일치
		if(userInfo.getLoginErrCnt() >= 5) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 5회 이상 일치하지 않아 사용할 수 없는 계정입니다. 담당자에게 연락주시기 바랍니다.");
		}
		
		try {
			// 인증 서버 인증 요청 
			AccessTokenResponse accessTokenResponse = authProvider.grantToken(login.getUsername(), login.getPassword());
			log.info("USERNAME: [{}]", accessTokenResponse);
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
			userMapper.updateLoginSuccess(User.builder()
					.lastLoginIp(login.getLoginIp()).userSeq(userInfo.getUserSeq()).build());
			
			return accessTokenResponse;
		} catch(InternalServerErrorException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "인증 서버에 연결할 수 없습니다.");
		}  catch(RedisConnectionFailureException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "로그인 서버에 연결할 수 없습니다.");
		} catch(NotAuthorizedException e) {
			userMapper.updateLoginFailure(User.builder()
					.userId(userInfo.getUserId()).build());
			log.info("USERNAME: [{}], LOGIN ERROR COUNT: [{}]", userInfo.getUserId(), userInfo.getLoginErrCnt() + 1);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "등록되지 않은 사용자 또는 잘못된 비밀번호입니다.");
		} catch(Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
		
	}
	
	@Override
	public ReissueResponse reissue(Reissue reissue) {
        IntrospectResponse introspect = authProvider.tokenIntrospect(reissue.getRefreshToken());
        log.info("[Introspect Response] <{}>", introspect);
        
        if(introspect == null || !introspect.isActive()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰 검증에 실패했습니다.");
        }
        
        LoginUserInfo loginUserInfo = objectMapper.convertValue(redis.getHash(introspect.getSub(), "user"), LoginUserInfo.class);
        if(loginUserInfo == null || !loginUserInfo.getLoginIp().equals(reissue.getLoginIp())) {
        	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 아이피와 요청 아이피가 일치하지 않습니다.");
        }
        
		String storedRefreshtoken = (String) redis.getHash(introspect.getSub(), "refreshToken");
		if(storedRefreshtoken == null || !storedRefreshtoken.equals(reissue.getRefreshToken())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "요청한 리프레시 토큰과 서버에 저장된 리프레시 토큰이 일치하지 않습니다.");
		}
    	
    	return authProvider.reissue(reissue.getRefreshToken());
	}

}
