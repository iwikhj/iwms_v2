# iwms_v2

## 개발 환경
 - spring boot: 2.7.8
   - spring-core: 5.3.25
   - lombok: 1.18.24
 - java: 11
 - mariaDB: 10.10.2
 - tomcat: 9.0.71 (Embedded)

## API DOCS
 - http://localhost/apidocs

## API 요청 플로우
 - Filter 
1. CorsFilter: allow cross origin 
2. JwtAuthenticationFilter: Spring security filter전에 호출. Access token validation check. (EXPIRED: refresh token 요청. INVALID: reject)
3. UsernamePasswordAuthenticationFilter: Spring security filter. access token 검증(keycloak)

 - ArgumentResolver
4. Redis 서버에 저장된 로그인 사용자 정보 매핑(key: Authentication name)
	
 - Controller
5. 파라미터 유효성 검사: @Valid
6. Logging: @Aspect
7. 비지니스 로직 처리
8. Response

 - Error: @RestControllerAdvice -> @ExceptionHandler

## 기타
 - 로그인(성공)
1. 로그인 요청: ID, PWD
2. IWMS DB 서버에 등록된 ID인지 확인
3. 인증 서버(Keycloak)에 인증 토큰 발급 요청
4. Access Token 및 Refresh Token 발행
5. 발급된 Refresh Token은 Redis에 저장
6. Redis에 로그인 사용자의 정보를 저장: 사용자 기본 정보, 허가된 메뉴 정보, 허가된 프로젝트 or 사이트 정보
7. 로그인 성공 기록 DB에 저장: 접속 아이피, 로그인 에러 카운트 초기화

 - 인증, 인가
1. Spring Security Filter에서 토큰 검증: Header [Authorization: Bearer {access token}]
2. 인증: 토큰의 유효성 검증
3. 인가: 인증된 토큰의 권한에 따라 접근 제한

 - Refresh Token
1. Spring Security Filter 호출전에 Access Token 유효성 검사를 진행
2. Access Token 만료시간 유효성 검증
3. Refresh Token 유효성 검증
4. Redis에 저장된 Refresh Token과 검증된 Refresh Token의 일치 여부 확인
5. Access Token 재발급
6. 발급된 Access Token 응답 헤더에 등록
7. 현재 호출에 한해서 임시 인증서 발행
8. 이후 호출에 대해서는 재발급된 Access Token을 사용하여 인증, 인가 진행

 - 로그인한 사용자 정보
1. 인증, 인가된 모든 페이지 요청에 대해서는 Redis 서버에 저장된 로그인 사용자 정보를 함께 제공: 사용자 기본 정보, 허가된 메뉴 정보, 허가된 프로젝트 or 사이트 정보

 - 사용자 관리
1. 사용자 인증 정보는 Keycloak 서버에서 관리한다.(아이디, 비밀번호, 권한)
2. 사용자 인증 정보를 제외한 모든 사용자 정보는 IWMS DB 서버에서 관리한다.
