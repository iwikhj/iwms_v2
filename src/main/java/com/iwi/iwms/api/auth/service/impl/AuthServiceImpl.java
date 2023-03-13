package com.iwi.iwms.api.auth.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.domain.AuthMenu;
import com.iwi.iwms.api.auth.domain.AuthMenuInfo;
import com.iwi.iwms.api.auth.mapper.AuthMapper;
import com.iwi.iwms.api.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthMapper authMapper;

	@Override
	public List<AuthInfo> listAuth(Map<String, Object> map) {
		return authMapper.listAuth(map);
	}

	@Override
	public int countAuth(Map<String, Object> map) {
		return authMapper.countAuth(map);
	}

	@Override
	public AuthInfo getAuthBySeq(long authSeq) {
		return Optional.ofNullable(authMapper.getAuthBySeq(authSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다"));
	}

	@Override
	public AuthInfo getAuthByAuthCd(String authCd) {
		return Optional.ofNullable(authMapper.getAuthByAuthCd(authCd))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다"));
	}

	@Override
	public void insertAuth(Auth auth) {
		authMapper.insertAuth(auth);
	}

	@Override
	public int updateAuth(Auth auth) {
		Optional.ofNullable(authMapper.getAuthBySeq(auth.getAuthSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다"));
		
		return authMapper.updateAuth(auth);
	}

	@Override
	public int deleteAuth(Auth auth) {
		Optional.ofNullable(authMapper.getAuthBySeq(auth.getAuthSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다"));
		
		return authMapper.deleteAuth(auth);
	}

	@Override
	public List<AuthMenuInfo> getAuthMenuByAuthSeq(long authSeq) {
		Optional.ofNullable(authMapper.getAuthBySeq(authSeq))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다"));
		
		return authMapper.getAuthMenuByAuthSeq(authSeq);
	}
	
	@Override
	public void updateAuthMenu(AuthMenu authMenu) {
		authMapper.updateAuthMenu(authMenu);
	}
}
