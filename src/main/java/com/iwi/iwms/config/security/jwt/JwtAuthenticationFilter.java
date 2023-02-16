package com.iwi.iwms.config.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.common.errors.ErrorResponse;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.config.redis.RedisProvider;
import com.iwi.iwms.config.security.QuickGuideUser;
import com.iwi.iwms.utils.CookieUtil;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtDecoder jwtDecoder;
	
    private final RedisProvider redis;
    
    private final ObjectMapper objectMapper;
    
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    	final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String accessToken = resolveAuthHeader(request);
        
        // 테스트할때 token넣기 귀찮아서 임시로 사용
        if(!StringUtils.hasText(accessToken)) {
        	String ssoId = "fdca608f-b7a7-4b87-8a61-273b70bcc88a";
        	List<String> roles = new ArrayList<>();
            roles.add("ROLE_IWMS_ADMIN");
            
        	passAuthentication(ssoId, roles);
        }
        
        JwtCode accessTokenStatus = tokenValidation(accessToken);
        
        log.info("======================================================================================================================");
        log.info("============== REFRESH TOKEN: {}", CookieUtil.getCookie(request, "refresh_token"));
        log.info("======================================================================================================================");
        
        if(JwtCode.EXPIRED.equals(accessTokenStatus)) {
        	
    		/**
    		 * Access token reissue flow
    		 * 1. 쿠키에서 리프레시 토큰 불러오기
    		 * 2. 리프레시 토큰 유효성 검사
    		 * 3. 저장소(redis or db)에 저장된 리프레시 토큰 불러오기
    		 * 4. 저장된 리프레시 토큰과 쿠키에서 불러온 리프레시 토큰이 일치하는지 검사
    		 * 5. access token 재발급 후 쿠키에 저장
    		 * 6. 해당 호출에 한해서 passAuthentication으로 임시로 인증받기
    		 */
        	
        	String refreshToken = CookieUtil.getCookie(request, "refresh_token");
        	JwtCode refreshTokenStatus = tokenValidation(refreshToken);
        	
        	if(JwtCode.EXPIRED.equals(refreshTokenStatus)) {
                sendError(request, response, "리프레시 토큰이 만료되었습니다.");
                return;
        	} else if(JwtCode.INVALID.equals(refreshTokenStatus)) {
        		sendError(request, response, "인증에 실패했습니다.");
                return;
        	} else {
        		String ssoId = getSsoId(refreshToken);
        		LoginUserInfo loginUserInfo = objectMapper.convertValue(redis.getHash(ssoId, "user"), LoginUserInfo.class);
        		
        		if(loginUserInfo != null && refreshToken.equals(loginUserInfo.getRefreshToken())) {
        			log.info("reissue: {}", "리프레시 토큰은 확인했지만 재발급 로직은 없음~~"); 
        			//AccessTokenResponse accessTokenResponse = keycloakProvidor.reissue(ssoId);
        			
        			//List<String> roles = new ArrayList<>();
                    //roles.add(loginUserInfo.getUserRole());
        			//passAuthentication(ssoId, loginUserInfo.getRoles());
        			
        			//response.setHeader("access_token", accessTokenResponse.getToken());
        			
        		}else {
        			log.info("reissue: {}", "저장된 리프레시 토큰과 일치하지 않음");
        			sendError(request, response, "인증에 실패했습니다.");
        			return;
        		}
        	}
        } else if(JwtCode.INVALID.equals(accessTokenStatus)) {
        	//sendError(request, response, "인증에 실패했습니다.");
        	//return;
        }
        
        chain.doFilter(request,response);
	}
    
    private String resolveAuthHeader(HttpServletRequest request) {
    	String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.length() > 7 && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    private String getSsoId(String token) {
        try {
        	return jwtDecoder.decode(token).getSubject();
		} catch(JwtValidationException e) {
			//log.info("JwtValidationException: {}", e.getMessage()); 
		} catch(BadJwtException e) {
			//log.info("BadJwtException: {}", e.getMessage()); 
		} catch(JwtException e) {
			//log.info("JwtException: {}", e.getMessage());
		} catch(Exception e) {
		}
        return null;
    }
    
    private JwtCode tokenValidation(String token) {
        try {
        	jwtDecoder.decode(token);
        	return JwtCode.VERIFIED;
		} catch(JwtValidationException e) {
			//log.info("JwtValidationException: {}", e.getMessage()); 
			boolean expired = e.getErrors().stream()
					.anyMatch(v -> (v.getErrorCode().equals("invalid_token") && v.getDescription().indexOf("expired") != -1));
			
			if(expired) {
				return JwtCode.EXPIRED;
			}
		} catch(BadJwtException e) {
			//log.info("BadJwtException: {}", e.getMessage()); 
		} catch(JwtException e) {
			//log.info("JwtException: {}", e.getMessage());
		} catch(Exception e) {
		}
        
        return JwtCode.INVALID;
    }
    
    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMessage) {
    	try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(ErrorResponse.builder()
            		.request(request)
            		.status(HttpServletResponse.SC_UNAUTHORIZED)
            		.message(errorMessage)
            		.build()
            		.toJson());
    	} catch (IOException e) {
    		//log.info("IOException: {}", e.getMessage());
    	}
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

}
