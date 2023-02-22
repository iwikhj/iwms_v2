package com.iwi.iwms.filestorage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageResponse {

	@Schema(description = "원본 파일 이름") 
	private String originalFilename;
	
	@Schema(description = "실제 파일 이름") 
	private String filename;
	
	@Schema(description = "파일 링크")
    private String link;
	
	@Schema(description = "MimeType")
	private String type;
    
	@Schema(description = "파일 크기")
    private long size;
    
	@Schema(description = "생성 일시")
    private String lastModified;
}
