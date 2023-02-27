package com.iwi.iwms.config.security.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuickGuideUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5442453208531219304L;

	@Schema(description = "SSO ID")
	private String username;
	
	@Schema(description = "사용 안함")
    private String password;
	
	@Schema(description = "사용자 권한 목록")
    private Collection<? extends GrantedAuthority> authorities;
	
	@Schema(description = "계정 만료 여부. true: 만료 안됨, false: 만료")
    private boolean isAccountNonExpired;
	
	@Schema(description = "계정 잠김 여부. true: 잠기지 않음, false: 잠김")
    private boolean isAccountNonLocked;
	
	@Schema(description = "비밀번호 만료 여부. true: 만료 안됨, false: 만료")
	private boolean isCredentialsNonExpired;
	
	@Schema(description = "사용자 활성화 여부. true: 활성화, false: 비활성화")
    private boolean isEnabled;
}