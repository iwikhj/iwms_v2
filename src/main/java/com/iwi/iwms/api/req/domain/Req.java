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

	@Schema(hidden = true, description = "요청사항 SEQ")
	private Long reqSeq;
	
	@Schema(hidden = true, description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "사이트 SEQ 목록")
	private List<Long> sitesSeq;
	
	@Schema(hidden = true, description = "사이트 SEQ") 
	private long siteSeq;
	
	@NotNull(message = "요청사항 타입은 필수 입력 사항입니다")
	@Schema(description = "요청사항 타입 코드: [00: 일반, 99: 긴급]", allowableValues = {"00", "99"}) 
	private String reqTypeCd;
	
	@NotNull(message = "요청사항 구분은 필수 입력 사항입니다")
	@Schema(description = "요청사항 구분 코드: [01: 신규개발, 02: 단순수정, 03: 기능개선, 04: 기능추가, 05: 자료요청, 06: 데이터]", allowableValues = {"01", "02", "03", "04", "05", "06"}) 
	private String reqGbCd;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", message = "유지보수 완료 예정일의 날짜 형식이 유효하지 않습니다(YYYYMMDD)")
	@Schema(description = "요청사항 완료 예정일: YYYYMMDD")
	private String reqEndYmd;
    
	@Schema(description = "요청사항 제목") 
	private String reqTitle;
    
	@Schema(description = "요청사항 내용") 
	private String reqContent;
	
	@Schema(description = "첨부할 파일")
	private List<MultipartFile> files;
	
	@Schema(hidden = true, description = "첨부할 파일 정보") 
	private UploadFile fileInfo;
	
	@Schema(description = "첨부된 파일 SEQ")
	private List<Long> attachedFilesSeq;
	
	@Schema(description = "사용 여부", allowableValues = {"Y", "N"}) 
	private String useYn;
	
	@Schema(hidden = true, description = "로그인 사용자 SEQ") 
	private long loginUserSeq;
	
	public Req of(final LoginUserInfo loginUserInfo) {
		this.loginUserSeq = loginUserInfo.getUserSeq();
		
		this.fileInfo = new UploadFile();
		this.fileInfo.setFileRefTb("TB_REQ");
		this.fileInfo.setFileRefCol("REQ_SEQ");
		if(this.reqSeq != null && this.reqSeq != 0) {
			this.fileInfo.setFileRefSeq(this.reqSeq);
		}
		this.fileInfo.setLoginUserSeq(loginUserInfo.getUserSeq());
		return this;
	}
}
