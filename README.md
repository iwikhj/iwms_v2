# iwms_v2

## 개발 환경
 - spring boot: 2.7.8
   - spring-core: 5.3.25
   - lombok: 1.18.24
 - maven: 3.8.7 
 - java: 11
 - mariaDB: 10.10.2
 - tomcat: 9.0.71 (Embedded)

## API DOCS
 - http://localhost/apidocs

## API 요청 플로우
 - Filter 
1. CorsFilter
2. AuthenticationFilter
3. SecurityFilterChain

 - HandlerMethodArgumentResolver
4. RedisTemplate
	
 - Controller
5. @Valid
6. Logging: @Aspect
7. Business logic
8. Response
 - Ok: ResponseEntity<T>
 - Error: ExceptionHandler -> ResponseEntity<ErrorResponse>

