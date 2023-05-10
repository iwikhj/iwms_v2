package com.iwi.iwms.api.login.domain;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

	@JsonProperty("accessToken")
    protected String token;

	@JsonProperty("expiresIn")
    protected long expiresIn;

	@JsonProperty("refreshToken")
    protected String refreshToken;
	
	@JsonProperty("refreshExpiresIn")
    protected long refreshExpiresIn;
	
	@JsonProperty("tokenType")
    protected String tokenType;

	@JsonProperty("sessionState")
    protected String sessionState;

    @JsonProperty("scope")
    protected String scope;
    
    public TokenInfo of(AccessTokenResponse accessTokenResponse) {
		BeanUtils.copyProperties(accessTokenResponse, this);
		return this;
    }
}
