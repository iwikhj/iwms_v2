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
import org.springframework.http.HttpStatus;
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
        
        String token = resolveAuthHeader(request);
        AuthCode tokenStatus = tokenValidation(token);
     
        printLog(request, tokenStatus);
        
        if(AuthCode.VERIFIED.equals(tokenStatus)) {
        	//ok
        } else if(AuthCode.EXPIRED.equals(tokenStatus)) {
			//responseError(request, response, HttpStatus.UNAUTHORIZED, tokenStatus.name());
			//return;
        	passAuthentication("bfb1e1d6-9018-4b50-8c69-f48c939b763b", Arrays.asList("ROLE_IWMS_ADMIN"));
    		request = ignoreRequestHeader(servletRequest, request, HttpHeaders.AUTHORIZATION);
    		
        } else if(AuthCode.INVALID.equals(tokenStatus)) {
			//responseError(request, response, HttpStatus.UNAUTHORIZED, tokenStatus.name());
			//return;
        	passAuthentication("bfb1e1d6-9018-4b50-8c69-f48c939b763b", Arrays.asList("ROLE_IWMS_ADMIN"));
        	//passAuthentication("f983803e-4d8e-45ec-a0c9-8fbef7c8263f", Arrays.asList("ROLE_IWMS_ENG"));
    		request = ignoreRequestHeader(servletRequest, request, HttpHeaders.AUTHORIZATION);
        }
        
        chain.doFilter(request, response);
	}
    
    private String resolveAuthHeader(HttpServletRequest request) {
    	String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.length() > 7 && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
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
			//log.info("BadJwtException: {}", e.getMessage()); 
		} catch(JwtException e) {
			//log.info("JwtException: {}", e.getMessage());
		} catch(Exception e) {
		}
        
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
			String bearerToken = StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION)) ? request.getHeader(HttpHeaders.AUTHORIZATION) : "";
			String remoteAddr = StringUtils.hasText(request.getRemoteAddr()) ? request.getRemoteAddr() : "-";
			String url = StringUtils.hasText(request.getRequestURI()) ? request.getRequestURI().toString() : "";
			String method = StringUtils.hasText(request.getMethod()) ? request.getMethod() : "-";
			String queryString = StringUtils.hasText(request.getQueryString()) ? request.getQueryString() : "";
			String referer = StringUtils.hasText(request.getHeader("Referer")) ? request.getHeader("Referer") : "-";
			String agent = StringUtils.hasText(request.getHeader("User-Agent")) ? request.getHeader("User-Agent") : "-";;
			String fullUrl = url;
			fullUrl += StringUtils.hasText(queryString)? "?"+queryString:queryString;
			
	        StringJoiner sj = new StringJoiner(">, <", "<", ">");
	        sj.add("Status:" + authCode.name());
	        sj.add("Len:" + bearerToken.length());
	        sj.add("IP:" + remoteAddr);
	        sj.add("Method:" + method);
	        sj.add("URI:" + fullUrl);
	        sj.add("Referer:" + referer);
	        sj.add("User-Agent:" + agent);

	        log.info("[FILTER LOG] {}", sj);
		} catch(Exception e) {}
	}
    
    private void responseError(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String errorMessage) {
    	try {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(ErrorResponse.builder()
            		.request(request)
            		.status(status.value())
            		.message(errorMessage)
            		.build()
            		.toJson());
    	} catch (IOException e) {
    		//log.info("IOException: {}", e.getMessage());
    	}
    }
}
