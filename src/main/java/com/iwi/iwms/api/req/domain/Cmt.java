package com.iwi.iwms.api.req.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.login.domain.LoginInfo;

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
public class Cmt {
	
	@Schema(hidden = true, description = "요청사항 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 상세 SEQ")
	private Long reqDtlSeq;
	
	@Schema(hidden = true, description = "코멘트 SEQ")
	private Long cmtSeq;

	@Schema(description = "이력 SEQ")
	private long hisSeq;

	@Schema(description = "코멘트") 
	private String cmt;
	
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
	
	public Cmt of(final LoginInfo loginInfo) {
		this.loginUserSeq = loginInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		if(this.reqDtlSeq == null) {
			this.fileInfo.setFileRefTb("TB_REQ_CMT");
			this.fileInfo.setFileRefCol("REQ_CMT_SEQ");
		} else {
			this.fileInfo.setFileRefTb("TB_REQ_DTL_CMT");
			this.fileInfo.setFileRefCol("REQ_DTL_CMT_SEQ");
		}

		if(this.cmtSeq != null) {
			this.fileInfo.setFileRefSeq(this.cmtSeq);
		}
		
		this.fileInfo.setLoginUserSeq(loginInfo.getUserSeq());
		return this;
	}
}
