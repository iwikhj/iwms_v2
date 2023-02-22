package com.iwi.iwms.api.file.domain;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.login.domain.LoginUserInfo;

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
	
	//@Schema(description = "첨부 파일 테이블 코드: [REQ: 유지보수, REQ_DTL: 유지보수 상세, REQ_DTL_CMT: 유지보수 상세 코멘트, NOTICE: 공지사항]", allowableValues = {"REQ", "REQ_DTL", "REQ_DTL_CMT", "NOTICE"}) 
	
	@Schema(hidden = true, description = "파일 SEQ")
	private long fileSeq;
	
	@Schema(hidden = true, description = "첨부 파일 테이블명")
	private String fileRefTb;
	
	@Schema(hidden = true, description = "첨부 파일 컬럼명")
	private String fileRefCol;
	
	@Schema(hidden = true, description = "첨부 파일 키")
	private long fileRefSeq;
	
	@NotNull
	@Schema(description = "첨부 파일 구분 코드: [01: 일반]", allowableValues = {"01"}) 
	private String fileGbCd;
	
	@Schema(hidden = true, description = "첨부 파일 원본 이름")
	private String fileOrgNm;
	
	@Schema(hidden = true, description = "첨부 파일 저장 이름")
	private String fileRealNm;
	
	@Schema(hidden = true, description = "첨부 파일 저장 경로")
	private String fileRealPath;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "삭제자 SEQ") 
	private long updtSeq;
	
	public static UploadFile of(UploadFileInfo uploadFileInfo) {
		UploadFile uploadFile = new UploadFile();
		BeanUtils.copyProperties(uploadFileInfo, uploadFile);
		return uploadFile;
	}
}
