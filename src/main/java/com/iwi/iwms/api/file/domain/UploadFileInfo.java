package com.iwi.iwms.api.file.domain;

import com.iwi.iwms.utils.PropertyUtil;

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
	private int fileOrdOrder;
	
	@Schema(description = "첨부 파일 원본 이름")
	private String fileOrgNm;
	
	@Schema(description = "첨부 파일 저장 이름")
	private String fileRealNm;
	
	@Schema(description = "첨부 파일 저장 경로")
	private String fileRealPath;
	
	@Schema(description = "첨부 파일 다운로드 URI")
	private String fileDownloadUri;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = PropertyUtil.getProperty("app.root") + "/" + PropertyUtil.getProperty("app.version") + "/files/download" + fileDownloadUri;
	}
}
