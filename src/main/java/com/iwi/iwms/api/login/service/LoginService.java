package com.iwi.iwms.api.login.service;

import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.config.security.auth.ReissueResponse;
import com.iwi.iwms.config.security.auth.TokenResponse;

public interface LoginService {

	TokenResponse login(Login login);
	
	ReissueResponse reissue(Reissue reissue);
	
}
