package com.iwi.iwms.api.user.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.PasswordChange;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;
import com.iwi.iwms.api.user.mapper.UserMapper;
import com.iwi.iwms.api.user.service.UserService;
import com.iwi.iwms.config.security.keycloak.KeycloakProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	
	private final KeycloakProvider keycloakProvider;
	
	@Override
	public List<UserInfo> listUser(Map<String, Object> map) {
		return userMapper.findAll(map);
	}

	@Override
	public int countUser(Map<String, Object> map) {
		return userMapper.count(map);
	}

	@Override
	public UserInfo getUserBySeq(long userSeq) {
		return Optional.ofNullable(userMapper.findBySeq(userSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}
	
	@Override
	public boolean checkDuplicateUserId(String userId) {
		return keycloakProvider.existsUsername(userId);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertUser(User user) {
		//인증 서버에 사용자 등록
		String username = user.getUserId();
		String password = user.getUserPwd();
		String firstName = "iwms"; 
		String lastName = user.getUserNm(); 
		String email = null;
		String role = user.getUserRole();
		
		if(keycloakProvider.existsUsername(username)) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미 등록된 사용자 ID입니다.");
		}
		
		String ssoId = keycloakProvider.insertUser(username, password, firstName, lastName, email, role);
		
		try {
			user.setSsoId(ssoId);
			userMapper.save(user);
		} catch(Exception e) {
			keycloakProvider.deleteUser(ssoId);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateUser(UserUpdate userUpdate) {
		UserInfo userInfo = Optional.ofNullable(userMapper.findBySeq(userUpdate.getUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.update(userUpdate);
		
		if(result > 0) {
			
			//인증 서버의 사용자 이름 수정
			if(!userUpdate.getUserNm().equals(userInfo.getUserNm())) {
				String firstName = "iwms"; 
				String lastName = userUpdate.getUserNm(); 
				String email = null;
				keycloakProvider.updateUser(userInfo.getSsoId(), firstName, lastName, email);
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
		UserInfo userInfo = Optional.ofNullable(userMapper.findBySeq(user.getUserSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.delete(user);
		
		if(result > 0) {
			
			//인증 서버의 사용자 삭제
			keycloakProvider.deleteUser(userInfo.getSsoId());
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int changePassword(PasswordChange passwordChange) {
		UserInfo userInfo = Optional.ofNullable(userMapper.findBySeq(passwordChange.getUserSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.updatePassword(passwordChange.asUser());
		
		if(result > 0) {
				
			//인증 서버의 사용자 비밀번호 변경
			keycloakProvider.changePassword(userInfo.getSsoId(), passwordChange.getUserPwd());
		}
		return result;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int resetPassword(PasswordChange passwordChange) {
		UserInfo userInfo = Optional.ofNullable(userMapper.findBySeq(passwordChange.getUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		
		int result = userMapper.updatePassword(passwordChange.asUser());
		
		if(result > 0) {
			
			//인증 서버의 사용자 비밀번호를 사용자 아이디로 초기화
			keycloakProvider.changePassword(userInfo.getSsoId(), userInfo.getUserId());
		}
		return result;
	}

	@Override
	public LoginUserInfo getLoginUser(String ssoId) {
		return Optional.ofNullable(userMapper.findLoginUser(ssoId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
	}
	
}