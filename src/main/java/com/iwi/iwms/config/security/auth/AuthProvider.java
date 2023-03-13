package com.iwi.iwms.config.security.auth;

import java.util.Arrays;

import javax.validation.constraints.NotNull;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

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
	 * @param String username
	 * @param String password
	 * @return AccessTokenResponse
	 */
	public AccessTokenResponse grantToken(@NotNull String username, @NotNull String password) {
        // keycloak Connecting
        Keycloak keycloak = KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(authClientId)
                .username(username)
                .password(password)
                .build();
        
        TokenManager tokenManager = keycloak.tokenManager();
        AccessTokenResponse accessTokenResponse = tokenManager.grantToken();
        
        keycloak.close();
        
        return accessTokenResponse;
	}
	
	/**
	 * 액세스 토큰 재발급
	 * @param String refreshToken
	 * @return ReissueResponse
	 */
	public ReissueResponse reissue(@NotNull String refreshToken) {
		if(!StringUtils.hasText(refreshToken)) {
			return null;
		}
    	Call<ReissueResponse> call = authTokenApi.reissue(OAuth2Constants.REFRESH_TOKEN, refreshToken, authClientId);
		return retrofitProvider.execute(call);
	}
	
	/**
	 * 토큰 검사
	 * @param String token
	 * @return IntrospectResponse
	 */
	public IntrospectResponse tokenIntrospect(String token) {
		if(!StringUtils.hasText(token)) {
			return IntrospectResponse.builder()
					.active(false)
					.build();
		}
    	Call<IntrospectResponse> call = authTokenApi.introspect(token, clientId, clientSecret);
    	return retrofitProvider.execute(call);
	}
	
	/**
	 * keycloak 접속
	 * @return Keycloak
	 */
	private Keycloak connectKeycloak() {
        // keycloak Connecting
        return  KeycloakBuilder.builder()
        		.serverUrl(authServerUrl)
        		.realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
	}
	
	/**
	 * 사용자 아이디 중복 체크
	 * @param String username
	 * @return boolean
	 */
	public boolean existsUsername(String username) {
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        
        boolean result = usersResource.list().stream().anyMatch(v -> v.getUsername().equals(username));
        
        if(result) {
        	log.info("Username already registered: {}", username);
        } else {
        	log.info("Username available: {}", username);
        }
        
        keycloak.close();
        
        return result;
	}
	
	/**
	 * 인증서버 사용자 등록
	 * @param String username
	 * @param String password
	 * @param String firstName
	 * @param String lastName
	 * @param String email
	 * @param String roles
	 * @return String
	 */
	public String insertUser(@NotNull String username, @NotNull String password, String firstName, String lastName, String email, @NotNull String role) {
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        
        String ssoId = null;
        
        try {
            // Define user
            UserRepresentation idmUser = new UserRepresentation();
            idmUser.setEnabled(true);
            idmUser.setUsername(username);
            idmUser.setFirstName(StringUtils.hasText(firstName) ? firstName : "");
            idmUser.setLastName(StringUtils.hasText(lastName) ? lastName : "");
            idmUser.setEmail(StringUtils.hasText(email) ? email : "");
            idmUser.setEmailVerified(false);
			
            Response response = usersResource.create(idmUser);
            log.info("Response: {} {}", response.getStatus(), response.getStatusInfo());
            log.info("Call: {}", response.getLocation());
            
            // Get keycloak id
            ssoId = CreatedResponseUtil.getCreatedId(response);
            log.info("User created with userId: {}", ssoId);
		} catch(Exception e) {
			if(keycloak != null) {
				keycloak.close();
			}
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 등록에 실패했습니다.");
		}
        
        try {
            // Define password 
            CredentialRepresentation passwordRep = new CredentialRepresentation();
            passwordRep.setTemporary(false);
            passwordRep.setType(CredentialRepresentation.PASSWORD);
            passwordRep.setValue(password);
            
            UserResource userResource = usersResource.get(ssoId);
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
          
            return ssoId;
        } catch(Exception e) {
			this.deleteUser(ssoId);
			keycloak.close();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 등록에 실패했습니다.");
		}
	}
	
	/**
	 * 인증서버 사용자 정보 변경
	 * @param String ssoId
	 * @param String firstName
	 * @param String lastName
	 * @param String email
	 */
	public void updateUser(String ssoId, String firstName, String lastName, String email) {
		log.info("by userId: {}", ssoId);
		
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(ssoId);

        // Define user
        UserRepresentation idmUser = new UserRepresentation();
        idmUser.setFirstName(StringUtils.hasText(firstName) ? firstName : "");
        idmUser.setLastName(StringUtils.hasText(lastName) ? lastName : "");
        idmUser.setEmail(StringUtils.hasText(email) ? email : "");
        idmUser.setEmailVerified(false);        
        
        // update user
        userResource.update(idmUser);
        log.info("Updated a user by userId: {}", ssoId);
        
        keycloak.close();
	}
	
	/**
	 * 인증서버 사용자 권한 변경
	 * @param String ssoId
	 * @param String oldRole
	 * @param String newRole
	 */
	public void updateRole(@NotNull String ssoId, @NotNull String oldRole, @NotNull String newRole) {
		
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(ssoId);

        // Remove old Role
        RoleRepresentation oldRoleRep = realmResource.roles().get(oldRole).toRepresentation();
		userResource.roles().realmLevel().remove(Arrays.asList(oldRoleRep));
		log.info("Unassign roles: {}", oldRoleRep); 

        // Create new Role
		RoleRepresentation newRoleRep = realmResource.roles().get(newRole).toRepresentation();
        userResource.roles().realmLevel().add(Arrays.asList(newRoleRep));
        log.info("Assign roles: {}", newRoleRep);
        
        keycloak.close();
	}
	
	/**
	 * 인증서버 사용자 비밀번호 변경
	 * @param String ssoId
	 * @param String password
	 */
	public void changePassword(@NotNull String ssoId, @NotNull String password) {
		
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(ssoId);

        // Define password 
        CredentialRepresentation passwordRep = new CredentialRepresentation();
        passwordRep.setTemporary(false);
        passwordRep.setType(CredentialRepresentation.PASSWORD);
        passwordRep.setValue(password);
        
        // Set password credential
        userResource.resetPassword(passwordRep);
        log.info("Changed password.");
        
        keycloak.close();
	}
	
	/**
	 * 인증서버 사용자 삭제
	 * @param String ssoId
	 */
	public void deleteUser(@NotNull String ssoId) {
		
        Keycloak keycloak = connectKeycloak();
		
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(ssoId);
        
        //Delete User
        userResource.remove();
        log.info("Deleted a user by userId: {}", ssoId);
        
        keycloak.close();
	}
}
