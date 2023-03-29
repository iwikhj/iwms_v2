package com.iwi.iwms.api.req.enums;

import java.util.Arrays;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

import lombok.Getter;

@Getter
public enum ReqStatCode {

	REQUEST("01", "작업요청"),
	AGREE("02", "합의"),
	NEGO_CHANGE("03", "협의요청(작업수정)"),
	NEGO_ADD("04", "협의요청(요청추가)"),
	REJECT("05", "반려"),
	CANCEL("09", "취소")
	;
	
	private String code;
	
	private String message;

	private ReqStatCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ReqStatCode findByCode(String code) {
        return Arrays.stream(values())
        		.filter(v -> v.getCode().equals(code))
        		.findAny()
        		.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "일치하는 요청사항 상태 코드가 없습니다."));				    }
}
