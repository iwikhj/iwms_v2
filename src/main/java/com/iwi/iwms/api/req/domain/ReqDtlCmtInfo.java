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
public class ReqDtlCmtInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "요청사항 상세 코멘트 SEQ")
	private Long reqDtlCmtSeq;
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "요청사항 상세 코멘트") 
	private String reqDtlCmt;
	
	@Schema(description = "코멘트 입력 유저 SEQ") 
	private long userSeq;
	
	@Schema(description = "코멘트 입력 유저") 
	private String userNm;
	
	@Schema(description = "첨부된 파일 정보")
	private List<UploadFileInfo> attachedFiles;
	
	@Schema(description = "삭제 여부") 
	private String delYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
