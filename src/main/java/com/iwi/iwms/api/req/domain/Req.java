package com.iwi.iwms.api.req.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class Req {

	@Schema(hidden = true, description = "유지보수 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "유지보수 번호") 
	private String reqNo;

	@NotNull(message = "소속은 필수 입력 사항입니다")
	@Schema(description = "소속 SEQ")
	private long compSeq;
	
	@NotNull(message = "프로젝트는 필수 입력 사항입니다")
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;	
	
	@NotNull(message = "사이트는 필수 입력 사항입니다")
	@Schema(description = "사이트 SEQ")
	private long siteSeq;
	
	@NotNull
	@Schema(description = "유지보수 타입 코드: [00: 일반, 99: 긴급]", allowableValues = {"00", "99"}) 
	private String reqTypeCd;
	
	@NotNull
	@Schema(description = "유지보수 구분 코드: [00: 오류, 01: 개선, 02: 추가, 99: 기타]", allowableValues = {"00", "01", "02", "99"}) 
	private String reqGbCd;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "유지보수 완료 예정일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "유지보수 완료 예정일: YYYYMMDD")
	private String reqEndYmd;
	
	@Schema(description = "유지보수 내용") 
	private String reqContentsTxt;
	
	@Schema(hidden = true, description = "삭제(취소) 여부", allowableValues = {"Y", "N"}) 
	private String delYn;
	
	@Schema(hidden = true, description = "삭제(취소) 사유") 
	private String reasonTxt;
	
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
	
	public Req of(final LoginUserInfo loginUserInfo) {
		this.regSeq = loginUserInfo.getUserSeq();
		this.updtSeq = loginUserInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		this.fileInfo.setFileRefTb("TB_REQ");
		this.fileInfo.setFileRefCol("REQ_SEQ");
		if(this.reqSeq != null && this.reqSeq != 0) {
			this.fileInfo.setFileRefSeq(this.reqSeq);
		}
		this.fileInfo.setFileGbCd("01");
		this.fileInfo.setRegSeq(loginUserInfo.getUserSeq());
		this.fileInfo.setUpdtSeq(loginUserInfo.getUserSeq());
		return this;
	}
}
