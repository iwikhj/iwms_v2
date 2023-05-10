package com.iwi.iwms.api.login.service;

import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.api.login.domain.ReissueInfo;
import com.iwi.iwms.api.login.domain.TokenInfo;

public interface LoginService {

	TokenInfo login(Login login);
	
	ReissueInfo reissue(Reissue reissue);
	
}
