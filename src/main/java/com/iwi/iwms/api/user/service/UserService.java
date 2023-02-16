package com.iwi.iwms.api.user.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.PasswordChange;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;

public interface UserService {

	List<UserInfo> listUser(Map<String, Object> map);
	
	int countUser(Map<String, Object> map);

	UserInfo getUserBySeq(long userSeq);
	
	boolean checkDuplicateUserId(String userId);
	
	void insertUser(User user);
	
	int updateUser(UserUpdate userUpdate);
	
	int deleteUser(User user);
	
	int changePassword(PasswordChange passwordChange);
	
	int resetPassword(PasswordChange passwordChange);
	
	LoginUserInfo getLoginUser(String ssoId);
}
