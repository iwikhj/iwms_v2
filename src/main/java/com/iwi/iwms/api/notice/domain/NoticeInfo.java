package com.iwi.iwms.api.notice.domain;

import java.util.List;

import com.iwi.iwms.api.file.domain.UploadFileInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeInfo {

	@Schema(description = "공지사항 SEQ")
	private long noticeSeq;
	
	@Schema(description = "회사 SEQ")
	private Long compSeq;
	
	@Schema(description = "회사 이름")
	private String compNm;
	
	@Schema(description = "제목") 
	private String title;
	
	@Schema(description = "내용") 
	private String content;
	
	@Schema(description = "최상단 고정 여부") 
	private String fixedTopYn;
	
	@Schema(description = "메인상단 노출 여부") 
	private String mainTopYn;
	
	@Schema(description = "조회수") 
	private int viewCnt;
	
	@Schema(description = "첨부된 파일 정보")
	private List<UploadFileInfo> attachedFiles;
	
	@Schema(description = "삭제 여부") 
	private String delYn;
	
	@Schema(description = "등록 일자") 
	private String regDt;
	
	@Schema(description = "등록자") 
	private String regNm;
	
	@Schema(description = "수정 일자") 
	private String updtDt;
	
	@Schema(description = "수정자") 
	private String updtNm;
}
