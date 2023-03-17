package com.iwi.iwms.api.auth.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.auth.domain.Auth;
import com.iwi.iwms.api.auth.domain.AuthInfo;

public interface AuthService {
	
	List<AuthInfo> listAuth(Map<String, Object> map);
	
	int countAuth(Map<String, Object> map);
	
	AuthInfo getAuthBySeq(long authSeq, long loginUserSeq);
	
	AuthInfo getAuthByAuthCd(String authCd);
	
	int updateAuth(Auth auth);

}
