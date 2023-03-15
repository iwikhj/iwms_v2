package com.iwi.iwms.api.req.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum ReqStatCode {

	REQUEST("01", "작업요청"),
	AGREE("02", "합의"),
	NEGO("03", "협의요청"),
	REJECT("04", "반려"),
	CANCEL("05", "취소")
	;
	
	private String code;
	
	private String message;

	private ReqStatCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static String toMessage(String reqStatCd) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(reqStatCd)).findAny().get().getMessage();
    }
}
