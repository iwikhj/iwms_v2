package com.iwi.iwms.api.req.domain;

import java.util.List;

import com.iwi.iwms.api.file.domain.UploadFileInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReqInfo {

	@Schema(description = "번호")
	private long rowNum;
	
	@Schema(description = "요청사항 SEQ")
	private long reqSeq;
	
	@Schema(description = "요청사항 번호") 
	private String reqNo;
	
	@Schema(description = "요청사항 상세 SEQ")
	private long reqDtlSeq;
	
	@Schema(description = "요청사항 상세 번호") 
	private String reqDtlNo;
	
	@Schema(description = "요청사항 제목") 
	private String reqTitle;
	
	@Schema(description = "프로젝트 SEQ")
	private long projSeq;
	
	@Schema(description = "사이트 SEQ")
	private long siteSeq;
	
	@Schema(description = "사이트 이름")
	private String siteNm;
	
	@Schema(description = "요청일")
	private String reqYmd;
	
	@Schema(description = "완료 요청일")
	private String reqEndYmd;
	
	@Schema(description = "요청사항 타입 코드") 
	private String reqTypeCd;
	
	@Schema(description = "요청사항 타입") 
	private String reqType;
	
	@Schema(description = "요청사항 구분 코드") 
	private String reqGbCd;
	
	@Schema(description = "요청사항 구분") 
	private String reqGb;
	
	@Schema(description = "담당자 이름") 
	private String reqDtlUserNm;
	
	@Schema(description = "작업 구분 코드") 
	private String busiRollCd;
	
	@Schema(description = "작업 구분") 
	private String busiRoll;
	
	@Schema(description = "상태 코드") 
	private String statCd;

	@Schema(description = "상태") 
	private String stat;
	
	@Schema(description = "상태 코멘트") 
	private String statCmt;

	@Schema(description = "최종 상태") 
	private String finalStat;
	
	@Schema(description = "첨부된 파일 정보")
	private List<UploadFileInfo> attachedFiles;	
	
	@Schema(description = "작성자 여부") 
	private String ownerYn;
	
	@Schema(description = "삭제 여부") 
	private String delYn;
	
	@Schema(description = "사용 여부")
	private String useYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
}
