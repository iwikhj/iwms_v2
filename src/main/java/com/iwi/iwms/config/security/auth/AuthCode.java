package com.iwi.iwms.config.security.auth;

import lombok.Getter;

@Getter
public enum AuthCode {

	VERIFIED("Verified"),
	EXPIRED("Expired"),
	INVALID("Invalid")
    ;
	
	private String message;

	private AuthCode(String message) {
		this.message = message;
	}
}
