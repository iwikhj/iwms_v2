package com.iwi.iwms.api.file.enums;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;

public enum UploadType {

	REQUEST("request"),
	REQUEST_CMT("request"),
	REQUEST_TASK("request"),
	REQUEST_TASK_CMT("request"),
	NOTICE("notice"),
	PROFILE("profile")
	;
	
	private String path;

	private UploadType(String path) {
		this.path = path;
	}
	
	public String getPath(long first, long... others) {
		if(this == UploadType.REQUEST) {
			return this.path + "/" + first;
		} else if(this == UploadType.REQUEST_CMT) {
			return this.path + "/" + first + "/comment/" + others[0];
		} else if(this == UploadType.REQUEST_TASK) {
			return this.path + "/" + first + "/task/" + others[0];
		} else if(this == UploadType.REQUEST_TASK_CMT) {
			return this.path + "/" + first + "/task/" + others[0] + "/comment/" + others[1];
		} else if(this == UploadType.NOTICE) {
			return this.path + "/" + first;
		} else if(this == UploadType.PROFILE) {
			return this.path + "/" + first;
		} else {
			throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "업로드 경로가 정의되지 않았습니다.");
		}
	}
}
