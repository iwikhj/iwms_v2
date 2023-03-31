package com.iwi.iwms.config.security.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.common.errors.ErrorResponse;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {
	
	private final JwtDecoder jwtDecoder;
    
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /*
    	passAuthentication("7afcc9a8-6364-4a4d-b7cf-ad36ab62da53", Arrays.asList("ROLE_IWMS_ADMIN"));
    	//passAuthentication("55fd148f-8e00-4243-93d3-be518c287ce9", Arrays.asList("ROLE_IWMS_ENG"));
		request = ignoreRequestHeader(servletRequest, request, HttpHeaders.AUTHORIZATION);
		printLog(request, AuthCode.INVALID);
		
		chain.doFilter(request, response);
		*/
        
     	String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    	if(token == null) {
    		printLog(request, AuthCode.INVALID);
    		responseError(response, ErrorCode.AUTHENTICATION_HEADER_NOT_EXISTS);
    		return;
    	}
        
    	token = resolveToken(token);
        AuthCode tokenStatus = tokenValidation(token);
     
        printLog(request, tokenStatus);
        
        if(AuthCode.VERIFIED.equals(tokenStatus)) {
        	//ok
        } else if(AuthCode.EXPIRED.equals(tokenStatus)) {
    		responseError(response, ErrorCode.AUTHENTICATION_EXPIRED);
    		return;
    		
        } else if(AuthCode.INVALID.equals(tokenStatus)) {
    		responseError(response, ErrorCode.AUTHENTICATION_HEADER_MALFORMED);
    		return;
        }
        
        chain.doFilter(request, response);
	}
    
    private String resolveToken(String token) {
    	if(token != null && token.length() > 7 && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return "";
    }
    
    private AuthCode tokenValidation(String token) {
        try {
        	jwtDecoder.decode(token);
        	return AuthCode.VERIFIED;
		} catch(JwtValidationException e) {
			log.info("JwtValidationException: {}", e.getMessage()); 
			boolean expired = e.getErrors().stream()
					.anyMatch(v -> (v.getErrorCode().equals("invalid_token") && v.getDescription().indexOf("expired") != -1));
			
			if(expired) {
				return AuthCode.EXPIRED;
			}
		} catch(BadJwtException e) {
		} catch(JwtException e) {
		} catch(Exception e) {}
        
        return AuthCode.INVALID;
    }
    
    private void passAuthentication(String ssoId, List<String> roles) {
		// Security for authorities.
        Collection<GrantedAuthority> authorities =  roles.stream()
			.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        
        // Security UserDetails
        QuickGuideUser quickGuideUser = QuickGuideUser.builder()
        			.username(ssoId)
        			.isAccountNonExpired(true)
        			.isAccountNonLocked(true)
        			.isCredentialsNonExpired(true)
        			.isEnabled(true)
        			.build();

        // createAuthentication
        // spring security 내에서 가지고 있는 객체입니다. (UsernamePasswordAuthenticationToken)
        Authentication authentication = new UsernamePasswordAuthenticationToken(quickGuideUser, "", authorities);
        
        // security가 만들어주는 securityContextHolder 그 안에 authentication을 넣어줍니다.
        // security가 securitycontextholder에서 인증 객체를 확인하는데
        // JwtAuthenticationFilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않습니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    private HttpServletRequest ignoreRequestHeader(ServletRequest servletRequest, HttpServletRequest request, String header) {
    	return (HttpServletRequest) new HttpServletRequestWrapper(request) {
            private Set<String> headerNameSet;

            @Override
            public Enumeration<String> getHeaderNames() {
                if (headerNameSet == null) {
                    // first time this method is called, cache the wrapped request's header names:
                    headerNameSet = new HashSet<>();
                    Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
                    while (wrappedHeaderNames.hasMoreElements()) {
                        String headerName = wrappedHeaderNames.nextElement();
                        if (!header.equalsIgnoreCase(headerName)) {
                            headerNameSet.add(headerName);
                        }
                    }
                }
                return Collections.enumeration(headerNameSet);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (header.equalsIgnoreCase(name)) {
                    return Collections.<String>emptyEnumeration();
                }
                return super.getHeaders(name);
            }

            @Override
            public String getHeader(String name) {
                if (header.equalsIgnoreCase(name)) {
                    return null;
                }
                return super.getHeader(name);
            }
        };
    }
    
	private void printLog(HttpServletRequest request, AuthCode authCode) {
		try {
			String header = request.getHeader(HttpHeaders.AUTHORIZATION);
			String bearerToken = StringUtils.hasText(header) ? header : "";
			String remoteAddr = StringUtils.hasText(request.getRemoteAddr()) ? request.getRemoteAddr() : " ";
			String url = StringUtils.hasText(request.getRequestURI()) ? request.getRequestURI().toString() : "";
			String method = StringUtils.hasText(request.getMethod()) ? request.getMethod() : " ";
			String queryString = StringUtils.hasText(request.getQueryString()) ? request.getQueryString() : "";
			String referer = StringUtils.hasText(request.getHeader("Referer")) ? request.getHeader("Referer") : " ";
			String agent = StringUtils.hasText(request.getHeader("User-Agent")) ? request.getHeader("User-Agent") : " ";;
			String fullUrl = url + (StringUtils.hasText(queryString) ? ("?" + queryString) : "");
			
	        StringJoiner sj = new StringJoiner(">, <", "<", ">");
	        sj.add("Status:" + authCode.name());
	        sj.add("Token len:" + bearerToken.length());
	        sj.add("IP:" + remoteAddr);
	        sj.add("Method:" + method);
	        sj.add("URI:" + fullUrl);
	        sj.add("Referer:" + referer);
	        sj.add("User-Agent:" + agent);

	        log.info("[FILTER] {}", sj);
		} catch(Exception e) {}
	}
    
    private void responseError(HttpServletResponse response, ErrorCode code) throws IOException {
    	String er = ErrorResponse.builder()
	    		.code(code)
	    		.build()
	    		.toJson();
		
        response.setStatus(code.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(er);
    }
}
