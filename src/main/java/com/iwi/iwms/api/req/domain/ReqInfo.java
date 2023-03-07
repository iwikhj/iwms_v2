package com.iwi.iwms.api.req.domain;

import java.util.List;

import com.iwi.iwms.api.file.domain.UploadFileInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReqInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;

	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@Schema(description = "소속 이름")
	private String compNm;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;	
	
	@Schema(description = "프로젝트 이름")
	private String projNm;
	
	@Schema(description = "사이트 SEQ")
	private long siteSeq;
	
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "요청사항 등록일")
	private String reqYmd;
	
	@Schema(description = "요청사항 타입 코드") 
	private String reqTypeCd;
	
	@Schema(description = "요청사항 타입") 
	private String reqType;
	
	@Schema(description = "요청사항 구분 코드") 
	private String reqGbCd;
	
	@Schema(description = "요청사항 구분") 
	private String reqGb;

	@Schema(description = "요청사항 완료일 예정일")
	private String reqEndYmd;
	
	@Schema(description = "요청사항 내용") 
	private String reqContentsTxt;
	
	@Schema(description = "합의 여부") 
	private String agreeYn;
	
	@Schema(description = "합의 일시") 
	private String agreeDt;
	
	@Schema(description = "합의자 SEQ") 
	private long agreeUserSeq;
	
	@Schema(description = "합의자") 
	private String agreeUserNm;
	
	@Schema(description = "삭제(취소) 여부") 
	private String delYn;
	
	@Schema(description = "삭제(취소) 사유") 
	private String reasonTxt;
	
	@Schema(description = "첨부된 파일 정보")
	private List<UploadFileInfo> attachedFiles;
	
	@Schema(description = "요청사항 코멘트 목록")
	private List<ReqCmtInfo> comments;
	
	@Schema(description = "요청사항 상세 목록")
	private List<ReqDtlInfo> details;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
