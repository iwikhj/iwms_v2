package com.iwi.iwms.api.req.enums;

import java.util.Arrays;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

import lombok.Getter;

@Getter
public enum ReqDtlStatCode {
	
	RECEIPT("11", "접수"),
	IN_PROGRESS("12", "처리중"),
	PROCESSED("13", "처리완료"),
	INSPECTION_COMPLETED("14", "검수완료"),
	CANCEL("19", "취소")
    ;
	
	private String code;
	
	private String message;

	private ReqDtlStatCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ReqDtlStatCode findByCode(String code) {
        return Arrays.stream(values())
        		.filter(v -> v.getCode().equals(code))
        		.findAny()
        		.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "일치하는 요청사항 상세 상태 코드가 없습니다."));	
	}
}
