package com.iwi.iwms.api.req.enums;

import lombok.Getter;

@Getter
public enum ReqDtlStatCode {
	
	RECEIPT("11", "접수"),
	IN_PROGRESS("12", "처리중"),
	PROCESSED("13", "처리완료"),
	INSPECTION_COMPLETED("14", "검수완료"),
	CANCEL("15", "취소")
    ;
	
	private String code;
	
	private String message;

	private ReqDtlStatCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
