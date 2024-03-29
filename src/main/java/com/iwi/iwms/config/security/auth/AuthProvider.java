package com.iwi.iwms.config.security.auth;

import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.login.domain.ReissueInfo;
import com.iwi.iwms.config.retrofit.RetrofitProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthProvider {

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	
	@Value("${keycloak.realm}")
	private String realm;
	
	@Value("${keycloak.resource}")
	private String clientId;
	
	@Value("${keycloak.credentials.secret}")
	private String clientSecret;
	
	@Value("${keycloak.auth.client-id}")
	private String authClientId;
	
	private final RetrofitProvider retrofitProvider;
	
	private final AuthTokenApi authTokenApi;
	
	/**
	 * 토큰 발급
	 * @param id
	 * @param password
	 * @return AccessTokenResponse
	 * @exception ProcessingException | InternalServerErrorException: 인증 서버 접속 오류
	 * @exception NotAuthorizedException: 인증 실패
	 */
	public AccessTokenResponse grantToken(@NotNull String id, @NotNull String password) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(authClientId)
                .username(id)
                .password(password)
                .build()) {
			
	        TokenManager tokenManager = keycloak.tokenManager();
	        AccessTokenResponse accessTokenResponse = tokenManager.grantToken();
	        
	        return accessTokenResponse;
		} catch(NotAuthorizedException e) {
			throw new CommonException(ErrorCode.LOGIN_FAILED_INCORRECT_ID_PW);
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}  
	}
	
	/**
	 * 액세스 토큰 재발급
	 * @param refreshToken
	 * @return ReissueResponse
	 */
	public ReissueInfo reissue(@NotNull String refreshToken) {
    	Call<ReissueInfo> call = authTokenApi.reissue(OAuth2Constants.REFRESH_TOKEN, refreshToken, authClientId);
		return retrofitProvider.execute(call);
	}
	
	/**
	 * 토큰 검사
	 * @param token
	 * @return IntrospectResponse
	 */
	public IntrospectResponse tokenIntrospect(@NotNull String token) {
		if(!StringUtils.hasText(token)) {
			return IntrospectResponse.builder()
					.active(false)
					.build();
		}
    	Call<IntrospectResponse> call = authTokenApi.introspect(token, clientId, clientSecret);
    	return retrofitProvider.execute(call);
	}
	
	/**
	 * 아이디 중복 체크
	 * @param username
	 * @return {true} If the id has already been registered; {false} otherwise 
	 */
	public boolean idDuplicateCheck(@NotNull String id) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            
            boolean result = usersResource.list().stream().anyMatch(v -> v.getUsername().equals(id));
            log.info("Is this a duplicate ID? {}", result);
            
            return result;
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}        
	}
	
	/**
	 * 인증서버 사용자 등록
	 * @param id
	 * @param password
	 * @param name
	 * @param role
	 * @return ssoKey
	 */
	public String insertUser(@NotNull String id, @NotNull String password, String name, @NotNull String role) {
		String ssoKey = null;
		
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            
            // Define user
            UserRepresentation idmUser = new UserRepresentation();
            idmUser.setEnabled(true);
            idmUser.setUsername(id);
            idmUser.setFirstName(name);
            idmUser.setLastName("iwms");
    		
            Response response = usersResource.create(idmUser);
            log.info("Response: {} {}", response.getStatus(), response.getStatusInfo());
            log.info("Call: {}", response.getLocation());
            
            // Get keycloak id
            ssoKey = CreatedResponseUtil.getCreatedId(response);
            log.info("User created with userId: {}", ssoKey);
            
            // Define password 
            CredentialRepresentation passwordRep = new CredentialRepresentation();
            passwordRep.setTemporary(false);
            passwordRep.setType(CredentialRepresentation.PASSWORD);
            passwordRep.setValue(password);
            
            UserResource userResource = usersResource.get(ssoKey);
            userResource.resetPassword(passwordRep);
            log.info("Create password-type credentials.");
            
            // Create new Role
    		RoleRepresentation newRoleRep = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Arrays.asList(newRoleRep));
            log.info("Assign roles: {}", role);
            
            // Remove Default Role 
            RoleRepresentation defaultRoleRep = realmResource.roles().get("default-roles-master").toRepresentation();
            userResource.roles().realmLevel().remove(Arrays.asList(defaultRoleRep));
            log.info("Unassign roles: {}", defaultRoleRep);   
            
            return ssoKey;
		} catch(Exception e) {
			if(ssoKey != null) {
				this.deleteUser(ssoKey);
			}
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}
	}
	
	/**
	 * 인증서버 사용자 이름 변경
	 * @param ssoKey
	 * @param name
	 */
	public void updateUser(@NotNull String ssoKey, @NotNull String name) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            UserResource userResource = usersResource.get(ssoKey);
            
            // Define user
            UserRepresentation idmUser = new UserRepresentation();
            idmUser.setFirstName(name);
            
            // update user
            userResource.update(idmUser);
            log.info("Updated a user by userId: {}", ssoKey);
            
		} catch(NotFoundException e) {
			throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "인증 서버에서 사용자를 찾을 수 없습니다.");
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}          
	}
	
	/**
	 * 인증서버 사용자 권한 변경
	 * @param ssoKey
	 * @param oldRole
	 * @param newRole
	 */
	public void updateRole(@NotNull String ssoKey, @NotNull String oldRole, @NotNull String newRole) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            UserResource userResource = usersResource.get(ssoKey);
            
            // Remove old Role
            RoleRepresentation oldRoleRep = realmResource.roles().get(oldRole).toRepresentation();
    		userResource.roles().realmLevel().remove(Arrays.asList(oldRoleRep));
    		log.info("Unassign roles: {}", oldRoleRep); 

            // Create new Role
    		RoleRepresentation newRoleRep = realmResource.roles().get(newRole).toRepresentation();
            userResource.roles().realmLevel().add(Arrays.asList(newRoleRep));
            log.info("Assign roles: {}", newRoleRep);
            
		} catch(NotFoundException e) {
			throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "인증 서버에서 사용자를 찾을 수 없습니다.");
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}     
	}
	
	/**
	 * 인증서버 사용자 비밀번호 변경
	 * @param ssoKey
	 * @param password
	 */
	public void changePassword(@NotNull String ssoKey, @NotNull String password) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            UserResource userResource = usersResource.get(ssoKey);
            
            // Define password 
            CredentialRepresentation passwordRep = new CredentialRepresentation();
            passwordRep.setTemporary(false);
            passwordRep.setType(CredentialRepresentation.PASSWORD);
            passwordRep.setValue(password);
            
            // Set password credential
            userResource.resetPassword(passwordRep);
            log.info("Changed password.");
            
		} catch(NotFoundException e) {
			throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "인증 서버에서 사용자를 찾을 수 없습니다.");
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}  
	}
	
	/**
	 * 인증서버 사용자 삭제
	 * @param ssoKey
	 */
	public void deleteUser(@NotNull String ssoKey) {
		try (Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()) {
			
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            UserResource userResource = usersResource.get(ssoKey);
            
            //Delete User
            userResource.remove();
            log.info("Deleted a user by userId: {}", ssoKey);
            
		} catch(NotFoundException e) {
			throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "인증 서버에서 사용자를 찾을 수 없습니다.");
		} catch(Exception e) {
			throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "[Keycloak] " + e.getMessage());
		}  
	}
}
