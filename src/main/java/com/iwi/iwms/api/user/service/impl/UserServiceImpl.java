package com.iwi.iwms.api.user.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserPwd;
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
	public UserInfo getUserBySeq(long userSeq) {
		return Optional.ofNullable(userMapper.getUserBySeq(userSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"));
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
		String email = null;
		String role = user.getUserRole();
		
		if(keycloakProvider.existsUsername(username)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미 등록된 사용자 아이디입니다");
		}
		
		String ssoId = keycloakProvider.insertUser(username, password, firstName, lastName, email, role);
		
		try {
			user.setSsoId(ssoId);
			userMapper.insertUser(user);
		} catch(Exception e) {
			keycloakProvider.deleteUser(ssoId);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateUser(UserUpdate userUpdate) {
		UserInfo userInfo = Optional.ofNullable(userMapper.getUserBySeq(userUpdate.getUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.updateUser(userUpdate);
		
		if(result > 0) {
			
			//인증 서버의 사용자 이름 수정
			if(!userUpdate.getUserNm().equals(userInfo.getUserNm())) {
				String lastName = "iwms"; 
				String firstName = userUpdate.getUserNm(); 
				keycloakProvider.updateUser(userInfo.getSsoId(), firstName, lastName);
			}
			
			//인증 서버의 사용자 권한 수정
			if(!userUpdate.getUserRole().equals(userInfo.getUserRole())) {
				keycloakProvider.updateRole(userInfo.getSsoId(), userInfo.getUserRole(), userUpdate.getUserRole());
			}
		}
		
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteUser(User user) {
		UserInfo userInfo = Optional.ofNullable(userMapper.getUserBySeq(user.getUserSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.deleteUser(user);
		
		if(result > 0) {
			
			//인증 서버의 사용자 삭제
			keycloakProvider.deleteUser(userInfo.getSsoId());
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int passwordChange(UserPwd userPwd) {
		UserInfo userInfo = Optional.ofNullable(userMapper.getUserBySeq(userPwd.getUserSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"));
		
		int result = userMapper.updatePassword(userPwd);
		
		if(result > 0) {
			//인증 서버의 사용자 비밀번호 변경
			keycloakProvider.changePassword(userInfo.getSsoId(), userPwd.getUserPwd());
		}
		return result;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int passwordReset(UserPwd userPwd) {
		UserInfo userInfo = Optional.ofNullable(userMapper.getUserBySeq(userPwd.getUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"));
		
		int result = userMapper.updatePassword(userPwd);
		
		if(result > 0) {
			//인증 서버의 사용자 비밀번호를 사용자 아이디로 초기화
			keycloakProvider.changePassword(userInfo.getSsoId(), userInfo.getUserId());
		}
		return result;
	}

	@Override
	public LoginUserInfo getLoginUser(String ssoId) {
		return Optional.ofNullable(userMapper.getLoginUser(ssoId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다"));
	}
	
}
