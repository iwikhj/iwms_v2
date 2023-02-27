package com.iwi.iwms.config.security.auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthTokenApi {

	@FormUrlEncoded
	@POST("protocol/openid-connect/token") 
	Call<ReissueResponse> reissue(@Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token, @Field("client_id") String client_id);
	
	@FormUrlEncoded
	@POST("protocol/openid-connect/token/introspect") 
	Call<IntrospectResponse> introspect(@Field("token") String token, @Field("client_id") String client_id, @Field("client_secret") String client_secret);
}
