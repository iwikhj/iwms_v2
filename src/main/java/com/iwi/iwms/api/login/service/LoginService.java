package com.iwi.iwms.api.login.service;

import org.keycloak.representations.AccessTokenResponse;

import com.iwi.iwms.api.login.domain.Login;
import com.iwi.iwms.api.login.domain.Reissue;
import com.iwi.iwms.config.security.auth.ReissueResponse;

public interface LoginService {

	AccessTokenResponse login(Login login);
	
	ReissueResponse reissue(Reissue reissue);
	
}
