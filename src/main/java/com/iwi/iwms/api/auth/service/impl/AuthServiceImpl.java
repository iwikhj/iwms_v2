package com.iwi.iwms.api.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.domain.AuthMenu;
import com.iwi.iwms.api.auth.mapper.AuthMapper;
import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

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
	public AuthInfo getAuthBySeq(long authSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("authSeq", authSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(authMapper.getAuthBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "권한을 찾을 수 없습니다."));
	}

	@Override
	public AuthInfo getAuthByAuthCd(String authCd) {
		return Optional.ofNullable(authMapper.getAuthByAuthCd(authCd))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "권한을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateAuth(Auth auth) {
		AuthInfo authInfo = this.getAuthBySeq(auth.getAuthSeq(), auth.getLoginUserSeq());
		
		int menuSize = authInfo.getAuthMenus().stream()
				.map(v -> 1 + v.getSubMenus().size())
				.reduce((x, y) -> x + y)
				.get();
		
		if(CollectionUtils.isEmpty(auth.getAuthMenuSeq()) || auth.getAuthMenuSeq().size() != menuSize) {
			throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "모든 메뉴의 데이터를를 입력해주세요.");
		}
		
		for(int i = 0; i < auth.getAuthMenuSeq().size(); i++) {
			AuthMenu authMenu = AuthMenu.builder()
				.authMenuSeq(auth.getAuthMenuSeq().get(i))	
				.readYn(auth.getMenuReadYn().get(i))
				.writeYn(auth.getMenuWriteYn().get(i))
				.execYn(auth.getMenuExecYn().get(i))
				.useYn(auth.getMenuUseYn().get(i))
				.loginUserSeq(auth.getLoginUserSeq())
				.build();
			
			authMapper.updateAuthMenu(authMenu);
		}
		authMapper.updateAuth(auth);
		return 1;
	}
}

