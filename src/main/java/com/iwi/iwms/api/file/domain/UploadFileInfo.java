package com.iwi.iwms.api.file.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileInfo {
	
	@Schema(description = "파일 SEQ")
	private long fileSeq;
	
	@Schema(description = "첨부 파일 테이블명")
	private String fileRefTb;
	
	@Schema(description = "첨부 파일 컬럼명")
	private String fileRefCol;
	
	@Schema(description = "첨부 파일 키값")
	private String fileRefVal;
	
	@Schema(description = "첨부 파일 구분")
	private String fileGbCd;
	
	@Schema(description = "첨부 파일 정렬 순서")
	private String fileOrdNum;
	
	@Schema(description = "첨부 파일 원본 이름")
	private String fileOrgNm;
	
	@Schema(description = "첨부 파일 저장 이름")
	private String fileRealNm;
	
	@Schema(description = "첨부 파일 저장 경로")
	private String fileRealPath;
	
	@Schema(description = "삭제 여부") 
	private String delYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
}
