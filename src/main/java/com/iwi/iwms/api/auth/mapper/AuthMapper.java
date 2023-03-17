package com.iwi.iwms.api.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;
import com.iwi.iwms.api.auth.domain.AuthMenu;

@Mapper
public interface AuthMapper {
	
	List<AuthInfo> listAuth(Map<String, Object> map);
	
	int countAuth(Map<String, Object> map);
	
	AuthInfo getAuthBySeq(Map<String, Object> map);
	
	AuthInfo getAuthByAuthCd(String authCd);
	
	int updateAuth(Auth auth);
	
	void updateAuthMenu(AuthMenu authMenu);

}
