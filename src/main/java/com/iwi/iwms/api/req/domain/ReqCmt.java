package com.iwi.iwms.api.req.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.file.domain.UploadFile;
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
public class ReqCmt {

	@Schema(hidden = true, description = "요청사항 코멘트 SEQ")
	private Long reqCmtSeq;
	
	@Schema(hidden = true, description = "요청사항 SEQ")
	private long reqSeq;

	@Schema(description = "요청사항 코멘트") 
	private String reqCmt;
	
	@Schema(description = "첨부할 파일")
	private List<MultipartFile> files;
	
	@Schema(hidden = true, description = "첨부할 파일 정보") 
	private UploadFile fileInfo;
	
	@Schema(description = "첨부된 파일 SEQ")
	private List<Long> attachedFilesSeq;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public ReqCmt of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		this.fileInfo.setFileRefTb("TB_REQ_CMT");
		this.fileInfo.setFileRefCol("REQ_CMT_SEQ");
		if(this.reqCmtSeq != null && this.reqCmtSeq != 0) {
			this.fileInfo.setFileRefSeq(this.reqCmtSeq);
		}
		this.fileInfo.setFileGbCd("01");
		this.fileInfo.setRegSeq(loginUserInfo.getUserSeq());
		this.fileInfo.setUpdtSeq(loginUserInfo.getUserSeq());
		
		return this;
	}
}
