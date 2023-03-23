package com.iwi.iwms.api.common.errors;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class CommonException extends NestedRuntimeException {

	private final ErrorCode code;

	@Nullable
	private final String reason;

	public CommonException(ErrorCode code) {
		this(code, null);
	}

	public CommonException(ErrorCode code, @Nullable String reason) {
		super("");
		this.code = code;
		this.reason = reason;
	}

	public CommonException(ErrorCode code, @Nullable String reason, @Nullable Throwable cause) {
		super(null, cause);
		this.code = code;
		this.reason = reason;
	}

	@Override
	public String getMessage() {
		int msg = this.code.getStatus().value();
		String reason = this.reason != null ? " \"" + this.reason + "\"" : "";
		return NestedExceptionUtils.buildMessage(msg + reason, getCause());
	}

}
