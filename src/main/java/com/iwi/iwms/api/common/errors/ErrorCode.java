package com.iwi.iwms.api.common.errors;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import lombok.Getter;

@Getter
public enum ErrorCode {

	AUTHENTICATION_FAILED
	(HttpStatus.UNAUTHORIZED, "020", "인증 실패."),
	
	AUTHENTICATION_HEADER_NOT_EXISTS
	(HttpStatus.UNAUTHORIZED, "021", "인증 실패. 헤더에 토큰이 없음."),
	
	AUTHENTICATION_HEADER_MALFORMED
	(HttpStatus.UNAUTHORIZED, "022", "인증 실패. 토큰 형식 오류."),
	
	AUTHENTICATION_EXPIRED
	(HttpStatus.UNAUTHORIZED, "023", "인증 실패. 만료된 토큰."),
	
	AUTHENTICATION_REISSUE_FAILED
	(HttpStatus.UNAUTHORIZED, "024", "토큰 재발급 실패."),
	
	AUTHORIZATION_FAILED
	(HttpStatus.FORBIDDEN, "030", "허가되지 않은 요청."),

	PARAMETER_MALFORMED
	(HttpStatus.BAD_REQUEST, "040", "파라미터 오류."),	
	
	RESOURCES_NOT_EXISTS
	(HttpStatus.NOT_FOUND, "044", "리소스 없음."),
	
	STATUS_ERROR
	(HttpStatus.CONFLICT, "048", "상태 오류."),
	
	DUPLICATE_ERROR
	(HttpStatus.CONFLICT, "049", "중복 오류."),
	
	LOGIN_FAILED_INCORRECT_ID_PW
	(HttpStatus.UNAUTHORIZED, "050", "로그인 실패. 등록되지 않은 아이디 또는 잘못된 비밀번호."),
	
	LOGIN_FAILED_SUSPENDED_ID
	(HttpStatus.UNAUTHORIZED, "051", "로그인 실패. 사용 정지된 아이디."),
	
	LOGIN_FAILED_RETRY_EXCEEDED
	(HttpStatus.UNAUTHORIZED, "052", "로그인 실패. 로그인 재시도 허용 초과."),
	
	API_NOT_EXISTS
	(HttpStatus.NOT_FOUND, "080", "존재하지 않는 API."),
	
	INTERNAL_SERVER_ERROR
	(HttpStatus.INTERNAL_SERVER_ERROR, "090", "내부 시스템 오류."),
	
	INTERNAL_SERIVCE_ERROR
	(HttpStatus.SERVICE_UNAVAILABLE, "091", "내부 서비스 오류.")
	;
	
	private HttpStatus status; 
	
	private String code;
	
	private String desc;

	private ErrorCode(HttpStatus status, String code, String desc) {
		this.status = status;
		this.code = code;
		this.desc = desc;
	}
	
	public String getMessage(ErrorCode code, String message) {
		return StringUtils.hasText(message) ? code.getDesc() + " " + message : code.getDesc();
	}
}
