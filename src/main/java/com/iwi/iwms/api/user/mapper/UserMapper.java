package com.iwi.iwms.api.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.login.domain.LoginInfo;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserFindId;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserProjInfo;
import com.iwi.iwms.api.user.domain.UserPwd;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;

@Mapper
public interface UserMapper {

	List<UserInfo> listUser(Map<String, Object> map);
	
	int countUser(Map<String, Object> map);
	
	UserInfo getUserBySeq(Map<String, Object> map);
	
	UserInfo getUserById(String userId);
	
	void insertUser(User user);
	
	int updateUser(UserUpdate userUpdate);
	
	int deleteUser(User user);
	
	int updatePassword(UserPwd userPwd);
	
	int updateLoginSuccess(User user);
	
	int updateLoginFailure(User user);
	
	int updateNotiCheck(long userSeq);
	
	LoginInfo getLoginUser(String ssoKey);
	
	String getUserIdByNameTelComp(UserFindId userFindId);
	
	List<UserProjInfo> listProjByUserSeq(long userSeq);
	
	List<UserSiteInfo> listSiteByUserSeq(long userSeq);

}
