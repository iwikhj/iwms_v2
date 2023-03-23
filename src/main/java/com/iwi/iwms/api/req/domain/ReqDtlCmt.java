package com.iwi.iwms.api.req.domain;

import java.util.List;

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
public class ReqDtlCmt {

	@Schema(hidden = true, description = "요청사항 상세 코멘트 SEQ")
	private Long reqDtlCmtSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 마지막 상태 SEQ")
	private long reqDtlHisSeq;

	@Schema(description = "요청사항 상세 코멘트") 
	private String reqDtlCmt;
	
	@Schema(description = "첨부할 파일")
	private List<MultipartFile> files;
	
	@Schema(hidden = true, description = "첨부할 파일 정보") 
	private UploadFile fileInfo;
	
	@Schema(description = "첨부된 파일 SEQ")
	private List<Long> attachedFilesSeq;
	
	@Schema(description = "사용 여부", defaultValue = "Y", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public ReqDtlCmt of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		this.fileInfo.setFileRefTb("TB_REQ_DTL_CMT");
		this.fileInfo.setFileRefCol("REQ_DTL_CMT_SEQ");
		if(this.reqDtlCmtSeq != null && this.reqDtlCmtSeq != 0) {
			this.fileInfo.setFileRefSeq(this.reqDtlCmtSeq);
		}
		this.fileInfo.setLoginUserSeq(loginUserInfo.getUserSeq());
		return this;
	}
}
