package com.iwi.iwms.api.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.domain.AuthMenu;
import com.iwi.iwms.api.auth.domain.AuthMenuInfo;

@Mapper
public interface AuthMapper {
	
	List<AuthInfo> listAuth(Map<String, Object> map);
	
	int countAuth(Map<String, Object> map);
	
	AuthInfo getAuthBySeq(long authSeq);
	
	AuthInfo getAuthByAuthCd(String authCd);
	
	void insertAuth(Auth auth);
	
	int updateAuth(Auth auth);
	
	int deleteAuth(Auth auth);
	
	List<AuthMenuInfo> getAuthMenuByAuthSeq(long authSeq);

	void updateAuthMenu(AuthMenu authMenu);

}
