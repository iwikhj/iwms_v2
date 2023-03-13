package com.iwi.iwms.api.file.domain;

import org.springframework.beans.BeanUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
	
	@Schema(hidden = true, description = "파일 SEQ")
	private long fileSeq;
	
	@Schema(hidden = true, description = "첨부 파일 테이블명")
	private String fileRefTb;
	
	@Schema(hidden = true, description = "첨부 파일 컬럼명")
	private String fileRefCol;
	
	@Schema(hidden = true, description = "첨부 파일 키")
	private long fileRefSeq;
	
	@Schema(hidden = true, description = "첨부 파일 원본 이름")
	private String fileOrgNm;
	
	@Schema(hidden = true, description = "첨부 파일 저장 이름")
	private String fileRealNm;
	
	@Schema(hidden = true, description = "첨부 파일 저장 경로")
	private String fileRealPath;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	public static UploadFile of(UploadFileInfo uploadFileInfo) {
		UploadFile uploadFile = new UploadFile();
		BeanUtils.copyProperties(uploadFileInfo, uploadFile);
		return uploadFile;
	}
}
