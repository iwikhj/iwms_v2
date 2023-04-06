package com.iwi.iwms.config.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iwi.iwms.config.security.auth.AuthenticationEntryPointHandler;
import com.iwi.iwms.config.security.auth.AuthenticationFilter;
import com.iwi.iwms.config.security.auth.AuthorizationAccessDeniedHandler;
import com.nimbusds.jose.shaded.json.JSONArray;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Value("${app.root}/${app.version}")
	private String root;
	
	private String jwkSetUri;
	
	@Value("${keycloak.jwk-set-uri}")
    private void setJwkSetUri(String jwkSetUri
				    		, @Value("${keycloak.auth-server-url}") String authServerUrl
				    		, @Value("${keycloak.realm}") String realm) {
		this.jwkSetUri = authServerUrl + "/realms/" + realm + jwkSetUri;
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//matchUrlAndAuthority(http);
		
		http
			.csrf().disable()
			.exceptionHandling()
			.authenticationEntryPoint(new AuthenticationEntryPointHandler())
			.accessDeniedHandler(new AuthorizationAccessDeniedHandler())
			.and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
			.authorizeRequests()
			//jwtAuthenticationConverter 적용으로 scope를 통한 권한 관리는 disabled
			//.antMatchers("/**").hasAuthority("SCOPE_iwms")
			.anyRequest().authenticated()
			.and()
			.oauth2ResourceServer()
			.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
			.and()
			.addFilterBefore(new AuthenticationFilter(jwtDecoder()), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	private void matchUrlAndAuthority(HttpSecurity http) throws Exception {
		/*
		List<SecurityUrlMatcher> urlMatchers = repository.findAll();
        for (SecurityUrlMatcher matcher : urlMatchers) {
            http
                .authorizeRequests()
                .antMatchers(matcher.getUrl()).hasAuthority(matcher.getAuthority());
        }
        */
		http
		 	.authorizeRequests()
		 	.antMatchers(HttpMethod.POST, root + "/auths/**").hasRole("IWMS_ADMIN")
		 	.antMatchers(root + "/notices/**").hasAnyRole("IWMS_PM");
	}
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring()
	    		.antMatchers("/", root + "/login", root + "/reissue", "/apidocs/**", "/swagger-ui/**", "/naver/**")
	    		.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
		return jwtConverter;
	}

	public class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			JSONArray authorities = (JSONArray) jwt.getClaims().get("authorities");
			return authorities.stream().map(rn -> rn.toString().toUpperCase())
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}
	}
	
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
	}
}
