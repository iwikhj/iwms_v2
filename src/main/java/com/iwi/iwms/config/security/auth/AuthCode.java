package com.iwi.iwms.config.security.auth;

import lombok.Getter;

@Getter
public enum AuthCode {

	TOKEN_VERIFIED("Verified token"),
	TOKEN_EXPIRED("Expired token"),
	TOKEN_INVALID("Invalid token"),
	TOKEN_NOT_FOUND("Not found token")
    ;
	
	private String message;

	private AuthCode(String message) {
		this.message = message;
	}
}
