package com.iwi.iwms.api.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserProjInfo;
import com.iwi.iwms.api.user.domain.UserPwd;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;
import com.iwi.iwms.api.user.mapper.UserMapper;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.config.security.auth.AuthProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	
	private final AuthService authService;
	
	private final AuthProvider keycloakProvider;
	
	@Override
	public List<UserInfo> listUser(Map<String, Object> map) {
		return userMapper.listUser(map);
	}

	@Override
	public int countUser(Map<String, Object> map) {
		return userMapper.countUser(map);
	}

	@Override
	public UserInfo getUserBySeq(long userSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("userSeq", userSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(userMapper.getUserBySeq(map))
					.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "사용자를 찾을 수 없습니다."));
	}
	
	@Override
	public boolean checkExistsUserId(String userId) {
		return keycloakProvider.existsUsername(userId);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertUser(User user) {
		//인증 서버에 사용자 등록
		String username = user.getUserId();
		String password = user.getUserId();
		String lastName = "iwms"; 
		String firstName = user.getUserNm(); 
		String email = user.getUserEmail();
		
		AuthInfo authInfo = authService.getAuthByAuthCd(user.getAuthCd());
		String role = authInfo.getAuthCd();
		
		if(keycloakProvider.existsUsername(username)) {
			throw new CommonException(ErrorCode.DUPLICATE_ERROR, "이미 등록된 사용자 아이디입니다");
		}
		
		String ssoKey = keycloakProvider.insertUser(username, password, firstName, lastName, email, role);
		
		try {
			user.setAuthSeq(authInfo.getAuthSeq());
			user.setSsoKey(ssoKey);
			userMapper.insertUser(user);
		} catch(Exception e) {
			keycloakProvider.deleteUser(ssoKey);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateUser(UserUpdate userUpdate) {
		UserInfo userInfo = this.getUserBySeq(userUpdate.getUserSeq(), userUpdate.getLoginUserSeq());
		
		AuthInfo authInfo = authService.getAuthByAuthCd(userUpdate.getAuthCd());
		userUpdate.setAuthSeq(authInfo.getAuthSeq());
		
		int result = userMapper.updateUser(userUpdate);
		
		if(result > 0) {
			//인증 서버의 사용자 이름 수정
			if(!userUpdate.getUserNm().equals(userInfo.getUserNm())) {
				String lastName = "iwms"; 
				String firstName = userUpdate.getUserNm(); 
				String email = userUpdate.getUserEmail();
				
				keycloakProvider.updateUser(userInfo.getSsoKey(), firstName, lastName, email);
			}
			
			//인증 서버의 사용자 권한 수정
			if(userUpdate.getAuthSeq() != userInfo.getAuthSeq()) {
				keycloakProvider.updateRole(userInfo.getSsoKey(), userInfo.getAuthCd(), userUpdate.getAuthCd());
			}
		}
		
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteUser(User user) {
		UserInfo userInfo = this.getUserBySeq(user.getUserSeq(), user.getLoginUserSeq());

		int result = userMapper.deleteUser(user);
		
		if(result > 0) {
			//인증 서버의 사용자 삭제
			keycloakProvider.deleteUser(userInfo.getSsoKey());
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int passwordChange(UserPwd userPwd) {
		UserInfo userInfo = this.getUserBySeq(userPwd.getUserSeq(), userPwd.getLoginUserSeq());
		
		int result = userMapper.updatePassword(userPwd);
		
		if(result > 0) {
			//인증 서버의 사용자 비밀번호 변경
			keycloakProvider.changePassword(userInfo.getSsoKey(), userPwd.getUserPwd());
		}
		return result;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int passwordReset(UserPwd userPwd) {
		UserInfo userInfo = this.getUserBySeq(userPwd.getUserSeq(), userPwd.getLoginUserSeq());
		
		int result = userMapper.updatePassword(userPwd);
		
		if(result > 0) {
			//인증 서버의 사용자 비밀번호를 사용자 아이디로 초기화
			keycloakProvider.changePassword(userInfo.getSsoKey(), userInfo.getUserId());
		}
		return result;
	}

	@Override
	public LoginUserInfo getLoginUser(String ssoKey) {
		return Optional.ofNullable(userMapper.getLoginUser(ssoKey))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "로그인 정보를 찾을 수 없습니다."));				
	}

	@Override
	public List<UserProjInfo> listProjByUserSeq(long userSeq) {
		return userMapper.listProjByUserSeq(userSeq);
	}
	
	@Override
	public List<UserSiteInfo> listSiteByUserSeq(long userSeq) {
		return userMapper.listSiteByUserSeq(userSeq);
	}

}
