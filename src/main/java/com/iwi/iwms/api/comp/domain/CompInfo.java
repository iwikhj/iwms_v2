package com.iwi.iwms.api.comp.domain;

import java.util.List;

import com.iwi.iwms.api.user.domain.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "대표자명")
	private String compCeoNm;
	
	@Schema(description = "소속 구분 코드") 
	private String compGbCd;
	
	@Schema(description = "소속 구분") 
	private String compGb;
	
	@Schema(description = "연락처")
	private String compTel;
	
	@Schema(description = "비상연락처")
	private String compEmTel;
	
	@Schema(description = "계약 시작일")
	private String compStdYmd;
	
	@Schema(description = "계약 종료일")
	private String compEndYmd;
	
	@Schema(description = "소속 부서 목록")
	private List<DeptInfo> depts;
	
	@Schema(description = "소속 사용자 목록")
	private List<UserInfo> users;
	
	@Schema(description = "작성자 여부") 
	private String ownerYn;
	
	@Schema(description = "정지사유") 
	private String compStopReason;
	
	@Schema(description = "삭제 여부") 
	private String delYn;	
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String uptDt;
	
	@Schema(description = "수정자") 
	private String uptNm;	
}
