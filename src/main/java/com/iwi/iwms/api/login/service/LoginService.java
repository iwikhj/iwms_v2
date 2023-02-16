package com.iwi.iwms.api.login.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.representations.AccessTokenResponse;

import com.iwi.iwms.api.login.domain.Login;

public interface LoginService {

	AccessTokenResponse login(HttpServletRequest request, HttpServletResponse response, Login login);
	
}
