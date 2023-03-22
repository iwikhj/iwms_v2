package com.iwi.iwms.api.common.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	AUTHENTICATION_FAILED
	(HttpStatus.UNAUTHORIZED, "020", "인증 실패."),
	
	AUTHENTICATION_HEADER_NOT_EXISTS
	(HttpStatus.UNAUTHORIZED, "021", "인증 실패. 헤더에 토큰이 없음."),
	
	AUTHENTICATION_HEADER_MALFORMED
	(HttpStatus.UNAUTHORIZED, "022", "인증 실패. 토큰 형식이 잘못됨."),
	
	AUTHENTICATION_EXPIRED
	(HttpStatus.UNAUTHORIZED, "023", "인증 실패. 만료된 토큰."),
	
	AUTHENTICATION_REISSUE_FAILED
	(HttpStatus.UNAUTHORIZED, "024", "토큰 재발급 실패."),
	
	AUTHORIZATION_FAILED
	(HttpStatus.FORBIDDEN, "030", "권한 없음."),

	PARAMETER_MALFORMED
	(HttpStatus.BAD_REQUEST, "040", "파라미터 형식이 잘못됨."),	
	
	TARGET_DATA_NOT_EXISTS
	(HttpStatus.NOT_FOUND, "044", "대상 데이터 없음."),
	
	LOGIN_FAILED_INCORRECT_ID_PW
	(HttpStatus.UNAUTHORIZED, "050", "로그인 실패. 등록되지 않은 아이디 또는 잘못된 비밀번호."),
	
	LOGIN_FAILED_SUSPENDED_ID
	(HttpStatus.UNAUTHORIZED, "051", "로그인 실패. 사용 정지된 아이디."),
	
	LOGIN_FAILED_RETRY_EXCEEDED
	(HttpStatus.UNAUTHORIZED, "052", "로그인 실패. 로그인 재시도 허용 초과."),
	
	INTERNAL_SERVER_ERROR
	(HttpStatus.INTERNAL_SERVER_ERROR, "090", "서버 시스템 오류."),
	
	INTERNAL_SERIVCE_ERROR
	(HttpStatus.SERVICE_UNAVAILABLE, "091", "내부 서비스 오류.")
	;
	
	HttpStatus status; 
	
	private String code;
	
	private String desc;

	private ErrorCode(HttpStatus status, String code, String desc) {
		this.status = status;
		this.code = code;
		this.desc = desc;
	}
	
	public String getMessage(ErrorCode code, String message) {
		return message == null ? code.getDesc() : code.getDesc() + " " + message;
	}
}
