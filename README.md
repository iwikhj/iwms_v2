# iwms_v2

## 개발 환경
 - spring boot: 2.7.8
   - spring-core: 5.3.25
   - mariaDB-connector: 3.0.10
   - mybatis-starter: 2.3.0
   - lombok: 1.18.24

 - java: 11
 - mariaDB: 10.10.2
 - tomcat: 9.0.71 (embed tomcat)

## API DOCS
 - http://localhost/apidocs

## API 요청 플로우
 - Filter 
   1.CorsFilter: allow cross origin 
   2.JwtAuthenticationFilter: Spring security filter을 통한 인증전에 refresh token 확인을 위한 filter
   3.UsernamePasswordAuthenticationFilter: Spring security filter. access token 검증(keycloak)
1. 리스트1

2. 리스트2

3, 리스트3 
 - ArgumentResolver
   4.Redis 서버에 저장된 로그인 사용자 정보 매핑
	
 - Controller
   5.파라미터 유효성 검사: @Valid
   6.Logging: @Aspect

 - Error
   - @RestControllerAdvice -> @ExceptionHandler
