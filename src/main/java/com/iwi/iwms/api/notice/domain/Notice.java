package com.iwi.iwms.api.notice.domain;

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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

	@Schema(hidden = true, description = "공지사항 SEQ")
	private Long noticeSeq;
	
	@Schema(description = "회사 SEQ")
	private Long compSeq;
	
	@Schema(description = "제목") 
	private String title;
	
	@Schema(description = "내용") 
	private String content;
	
	@NotNull
	@Schema(description = "최상단 고정 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String fixedTopYn;
	
	@NotNull
	@Schema(description = "메인상단 노출 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String mainTopYn;
	
	@Schema(hidden = true, description = "조회수") 
	private int viewCnt;
	
	@Schema(description = "첨부할 파일")
	private List<MultipartFile> files;
	
	@Schema(hidden = true, description = "첨부할 파일 정보") 
	private UploadFile fileInfo;
	
	@Schema(description = "첨부된 파일 SEQ")
	private List<Long> attachedFilesSeq;
	
	@Schema(hidden = true, description = "삭제 여부", defaultValue = "N", allowableValues = {"Y", "N"}) 
	private String delYn;
	
	@Schema(hidden = true, description = "등록자 SEQ") 
	private long regSeq;
	
	@Schema(hidden = true, description = "수정자 SEQ") 
	private long updtSeq;
	
	public Notice of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		this.fileInfo.setFileRefTb("TB_NOTICE");
		this.fileInfo.setFileRefCol("NOTICE_SEQ");
		if(this.noticeSeq != null && this.noticeSeq != 0) {
			this.fileInfo.setFileRefSeq(this.noticeSeq);
		}
		this.fileInfo.setFileGbCd("01");
		this.fileInfo.setRegSeq(loginUserInfo.getUserSeq());
		this.fileInfo.setUpdtSeq(loginUserInfo.getUserSeq());
		
		return this;
	}

}
