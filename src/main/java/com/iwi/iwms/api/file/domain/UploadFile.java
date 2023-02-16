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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
	
	@NotNull
	@Schema(description = "첨부 파일 테이블 코드: [REQ: 유지보수, REQ_DTL: 유지보수 상세, REQ_DTL_CMT: 유지보수 상세 코멘트, NOTICE: 공지사항]", allowableValues = {"REQ", "REQ_DTL", "REQ_DTL_CMT", "NOTICE"}) 
	private String tableCd;
	
	@NotNull
	@Range(min = 0l, message = "Please select positive numbers Only")
	@Schema(description = "첨부 파일 테이블 SEQ") 
	private String tableRowSeq;
	
	@NotNull(message = "파일 데이터가 존재하지 않습니다.")
	@Schema(description = "업로드 파일 목록")
	private List<MultipartFile> multipartFiles;
	
	@Schema(hidden = true, description = "업로드 파일")
	private MultipartFile multipartFile;
	
	@Schema(hidden = true, description = "파일 SEQ")
	private long fileSeq;
	
	@Schema(hidden = true, description = "첨부 파일 테이블명")
	private String fileRefTb;
	
	@Schema(hidden = true, description = "첨부 파일 컬럼명")
	private String fileRefCol;
	
	@Schema(hidden = true, description = "첨부 파일이 사용되는 키값")
	private String fileRefVal;
	
	@NotNull
	@Schema(description = "첨부 파일 구분 코드: [01: 일반]", allowableValues = {"01"}) 
	private String fileGbCd;
	
	@Schema(hidden = true, description = "첨부 파일 정렬 순서")
	private String fileOrdNum;
	
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
	
	public UploadFile build(final UploadFile uploadFile, final MultipartFile multipartFile, final LoginUserInfo loginUserInfo) {
		if(multipartFile != null && !multipartFile.isEmpty()) {
			uploadFile.fileRefTb = "TB_" + uploadFile.getTableCd();
			uploadFile.fileRefCol = uploadFile.getTableCd() + "_SEQ";
			uploadFile.fileRefVal = uploadFile.getTableRowSeq();
			uploadFile.fileGbCd = uploadFile.getFileGbCd();
			
			uploadFile.multipartFile = multipartFile;
			uploadFile.fileOrgNm = multipartFile.getOriginalFilename();
			uploadFile.fileRealNm = fileRenameByUUID(multipartFile.getOriginalFilename());
			uploadFile.fileRealPath = getFileRealPath(uploadFile.getTableCd());
		} 
		uploadFile.regSeq = loginUserInfo.getUserSeq();
		uploadFile.updtSeq = loginUserInfo.getUserSeq();
		
		return uploadFile;
	}
	
	private String getFileRealPath(String tableCd) {
		if(tableCd.startsWith("REQ")) {
			return "request";
		} else if(tableCd.startsWith("NOTICE")) {
			return "notice";
		}
		return "";
	}
	
	private String fileRenameByUUID(String filename) {
		String body = UUID.randomUUID().toString().replace("-", "");
		String ext = null;

		int dot = filename.lastIndexOf(".");
		if (dot != -1) {
			ext = filename.substring(dot); // includes "."
		} else {
			ext = "";
		}
		return body + ext;
	}
	
	public static UploadFile of(UploadFileInfo uploadFileInfo) {
		UploadFile uploadFile = new UploadFile();
		BeanUtils.copyProperties(uploadFileInfo, uploadFile);
		return uploadFile;
	}
}
