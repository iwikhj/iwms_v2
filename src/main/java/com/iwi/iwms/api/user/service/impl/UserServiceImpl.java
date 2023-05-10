package com.iwi.iwms.api.user.service.impl;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.service.AuthService;
import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.enums.UploadType;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserFindId;
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
	
	private final FileService fileService;
	
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
	public UserInfo getUserById(String userId) {
		return userMapper.getUserById(userId);
	}
	
	@Override
	public boolean checkExistsUserId(String userId) {
		return userMapper.getUserById(userId) != null || keycloakProvider.idDuplicateCheck(userId);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertUser(User user) {
		//인증 서버에 사용자 등록
		String id = user.getUserId();
		String password = user.getUserId();
		String name = user.getUserNm(); 
		
		AuthInfo authInfo = authService.getAuthByAuthCd(user.getAuthCd());
		String role = authInfo.getAuthCd();
		
		if(this.checkExistsUserId(id)) {
			throw new CommonException(ErrorCode.DUPLICATE_ERROR, "이미 등록된 아이디입니다.");
		}
		
		String ssoKey = keycloakProvider.insertUser(id, password, name, role);
		
		try {
			user.setAuthSeq(authInfo.getAuthSeq());
			user.setSsoKey(ssoKey);
			userMapper.insertUser(user);
			
			// 프로필 사진 파일 저장
			if(user.getFile() != null && !user.getFile().isEmpty()) {
				if(user.getFile().getContentType().indexOf("image") == -1) {
					throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "프로필 사진은 이미지 파일만 업로드 가능합니다.");
				}
				UploadFile uploadFile = user.getFileInfo();
				uploadFile.setFileRefSeq(user.getUserSeq());
				uploadFile.setFileRealPath(UploadType.PROFILE.getPath(user.getUserSeq()));
				fileService.insertFiles(Arrays.asList(user.getFile()), uploadFile);
			}
		} catch(CommonException e) {
			keycloakProvider.deleteUser(ssoKey);
			throw new CommonException(e.getCode(), e.getReason());
		} catch(Exception e) {
			keycloakProvider.deleteUser(ssoKey);
			throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
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
			//인증 서버 사용자 이름 수정
			if(!userUpdate.getUserNm().equals(userInfo.getUserNm())) {
				keycloakProvider.updateUser(userInfo.getSsoKey(), userUpdate.getUserNm());
			}
			
			//인증 서버 사용자 권한 수정
			if(userUpdate.getAuthSeq() != userInfo.getAuthSeq()) {
				keycloakProvider.updateRole(userInfo.getSsoKey(), userInfo.getAuthCd(), userUpdate.getAuthCd());
			}
		}
		
		// 등록된 프로필 사진 파일 존재 여부
		boolean hasExistsFile = userUpdate.getFileSeq() != null;
		// 프로필 사진 파일 존재 여부
		boolean hasFile = userUpdate.getFile() != null && !userUpdate.getFile().isEmpty();
		
		if(hasExistsFile) {
			// 프로필 사진 파일 삭제
			List<UploadFileInfo> files = fileService.listFileByRef(userUpdate.getFileInfo());
			if(!CollectionUtils.isEmpty(files)) {
				fileService.deleteFiles(files, null);
			}
			
			// 프로필 사진 파일 저장
			if(hasFile) {
				if(userUpdate.getFile().getContentType().indexOf("image") == -1) {
					throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "프로필 사진은 이미지 파일만 업로드 가능합니다.");
				}
				UploadFile uploadFile = userUpdate.getFileInfo();
				uploadFile.setFileRealPath(UploadType.PROFILE.getPath(userUpdate.getUserSeq()));
				fileService.insertFiles(Arrays.asList(userUpdate.getFile()), uploadFile);
			}
		} else {
			// 프로필 사진 파일 저장
			if(hasFile) {
				if(userUpdate.getFile().getContentType().indexOf("image") == -1) {
					throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "프로필 사진은 이미지 파일만 업로드 가능합니다.");
				}
				UploadFile uploadFile = userUpdate.getFileInfo();
				uploadFile.setFileRealPath(UploadType.PROFILE.getPath(userUpdate.getUserSeq()));
				fileService.insertFiles(Arrays.asList(userUpdate.getFile()), uploadFile);
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
			//인증 서버 사용자 삭제
			keycloakProvider.deleteUser(userInfo.getSsoKey());
			
			// 프로필 사진 파일 삭제
			List<UploadFileInfo> files = fileService.listFileByRef(user.getFileInfo());
			if(!CollectionUtils.isEmpty(files)) {
				fileService.deleteFiles(files, null);
			}
			
			// 폴더 삭제
			fileService.deleteFolder(Paths.get(UploadType.PROFILE.getPath(user.getUserSeq())));
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int passwordChange(UserPwd userPwd) {
		UserInfo userInfo = this.getUserBySeq(userPwd.getUserSeq(), userPwd.getLoginUserSeq());
		
		int result = userMapper.updatePassword(userPwd);
		
		if(result > 0) {
			//인증 서버 사용자 비밀번호 변경
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
			//인증 서버 사용자 비밀번호를 사용자 아이디로 변경
			keycloakProvider.changePassword(userInfo.getSsoKey(), userInfo.getUserId());
		}
		return result;
	}
	
	@Override
	public int updateLoginSuccess(User user) {
		return userMapper.updateLoginSuccess(user);
	}

	@Override
	public int updateLoginFailure(User user) {
		return userMapper.updateLoginFailure(user);
	}

	@Override
	public int updateNotiCheck(long userSeq) {
		return userMapper.updateNotiCheck(userSeq);
	}	

	@Override
	public LoginInfo getLoginUser(String ssoKey) {
		return Optional.ofNullable(userMapper.getLoginUser(ssoKey))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "로그인 정보를 찾을 수 없습니다."));				
	}
	
	@Override
	public String getUserIdByNameTelComp(UserFindId userFindId) {
		return Optional.ofNullable(userMapper.getUserIdByNameTelComp(userFindId))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "등록된 사용자를 찾을 수 없습니다."));		
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
