package com.iwi.iwms.api.user.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserFindId {
	
	@NotNull(message = "사용자 이름은 필수 입력 사항입니다")
	@Schema(description = "사용자 이름")
	private String userNm;
	
	@NotNull(message = "전화번호는 필수 입력 사항입니다")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호가 아닙니다")
	@Schema(description = "전화번호") 
	private String userTel;
	
	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ") 
	private Long compSeq;
}
