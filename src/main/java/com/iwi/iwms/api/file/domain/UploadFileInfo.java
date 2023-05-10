package com.iwi.iwms.api.file.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	@Schema(description = "첨부 파일 정렬 순서")
	private int fileOrderNo;
	
	@Schema(description = "첨부 파일 원본 이름")
	private String fileOrgNm;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "첨부 파일 저장 이름")
	private String fileRealNm;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "첨부 파일 저장 경로")
	private String fileRealPath;
	
	@Schema(description = "첨부 파일 다운로드 패스")
	private String fileDownloadPath;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	//${app.path}/files
	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = "/files/download" + fileDownloadPath;
	}
}
