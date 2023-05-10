package com.iwi.iwms.config.retrofit;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iwi.iwms.config.security.auth.AuthTokenApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {
	
	private String baseUrl;
	
	@Value("${keycloak.auth-server-url}")
    private void setBaseUrl(String authServerUrl, @Value("${keycloak.realm}") String realm) {
		this.baseUrl = authServerUrl + "/realms/" + realm + "/";
    }
	
	@Bean
	public Retrofit retrofit() {
		return new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.client(unsafeOkHttpClient().build())
				.build();
	}
	
	@Bean
	public AuthTokenApi authTokenApi(Retrofit retrofit) {
		return retrofit.create(AuthTokenApi.class);
	}
	
	@Bean
	protected OkHttpClient.Builder unsafeOkHttpClient() {
		try {
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}
			} };

			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.readTimeout(5, TimeUnit.SECONDS);
			builder.connectTimeout(5, TimeUnit.SECONDS);
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			return builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
