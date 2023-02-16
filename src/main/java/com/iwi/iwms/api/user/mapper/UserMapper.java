package com.iwi.iwms.api.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.user.domain.User;
import com.iwi.iwms.api.user.domain.UserInfo;
import com.iwi.iwms.api.user.domain.UserUpdate;

@Mapper
public interface UserMapper {

	List<UserInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	UserInfo findBySeq(long userSeq);
	
	UserInfo findById(String userId);
	
	void save(User user);
	
	int update(UserUpdate userUpdate);
	
	int delete(User user);
	
	int updatePassword(User user);
	
	int updateLoginSuccess(User user);
	
	int updateLoginFailure(User user);
	
	LoginUserInfo findLoginUser(String ssoId);
	
	

}
