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
public class ReqCmtInfo {
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 코멘트 SEQ")
	private Long reqCmtSeq;

	@Schema(description = "요청사항 코멘트") 
	private String reqCmt;
	
	@Schema(description = "요청사항 상태 코드") 
	private String reqStatCd;
	
	@Schema(description = "요청사항 상태") 
	private String reqStat;
	
	@Schema(description = "첨부된 파일 정보")
	private List<UploadFileInfo> attachedFiles;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
}
