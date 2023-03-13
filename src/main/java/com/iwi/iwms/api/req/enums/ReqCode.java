package com.iwi.iwms.api.req.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public enum ReqCode {

	REQ("작업요청"),
	AGREE("합의"),
	NEGO("협의요청"),
	REJECT("반려"),
	CANCEL("취소")
    ;
	
	private String message;

	private ReqCode(String message) {
		this.message = message;
	}
	
	public static ReqCode getReqCode(String reqStatCd) {
		if("Y".equals(reqStatCd)) {
			return ReqCode.AGREE;
		} else if("N".equals(reqStatCd)) {
			return ReqCode.NEGO;
		} else if("R".equals(reqStatCd)) {
			return ReqCode.REJECT;
		} else if("C".equals(reqStatCd)) {
			return ReqCode.CANCEL;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 코드를 찾을 수 없습니다.");
	}
}
