package com.iwi.iwms.config.security.jwt;

import lombok.Getter;

@Getter
public enum JwtCode {

	VERIFIED("Verified"),
	EXPIRED("Expired"),
	INVALID("Invalid")
    ;
	
	private String message;

	private JwtCode(String message) {
		this.message = message;
	}
}
