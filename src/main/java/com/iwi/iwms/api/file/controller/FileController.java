package com.iwi.iwms.api.file.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.filestorage.FileStorageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "File", description = "IWMS 파일 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.root}/${app.version}/files")
public class FileController {
	
	private final FileService fileService;
	
    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileStorageResponse>> upload(HttpServletRequest request
			//, @Parameter(hidden = true) LoginUserInfo loginUserInfo	
			, @RequestParam(value = "file", required = true) MultipartFile multipartFile) {
    	
    	String ms = String.valueOf(System.currentTimeMillis()) ;
    	String path1 = ms.substring(0, 3);
    	String path2 = ms.substring(3, 6);
    	
		Path path = Paths.get(path1).resolve(path2);
		FileStorageResponse fileStorageResponse = fileService.upload(multipartFile, path);
		
		return ResponseEntity.ok(ApiResponse.<FileStorageResponse>builder()
				.request(request)
				.data(fileStorageResponse)
				.build());
    }
    
	@Operation(hidden = true, summary = "파일 링크", description = "파일 링크")
	@GetMapping(value = "/link/{path1}/{path2}/{filename:.+}")
	public ResponseEntity<Resource> link(HttpServletRequest request
			//, @Parameter(hidden = true) LoginUserInfo loginUserInfo	
			, @PathVariable String path1
			, @PathVariable String path2
			, @PathVariable String filename) throws IOException {
		
		Path path = Paths.get(path1).resolve(path2).resolve(filename);
		Resource resource = fileService.getFileResource(path);
		
		File file = resource.getFile();
		
		MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
		
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		if(contentType != null) {
			mediaType = MediaType.parseMediaType(contentType);
		}
		log.info("Content Type: {}", mediaType);
		
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.TRANSFER_ENCODING, "binary");
        
        return ResponseEntity.ok()
        		.cacheControl(CacheControl.noCache())
                .contentType(mediaType)
                .headers(headers)
                .body(resource);
	}
    
    
	@Operation(hidden = true, summary = "파일 다운로드", description = "파일 다운로드")
	@GetMapping(value = "/download/{fileSeq}")
	public ResponseEntity<Resource> download(HttpServletRequest request
			//, @Parameter(hidden = true) LoginUserInfo loginUserInfo	
			, @PathVariable long fileSeq) throws IOException {
		
		UploadFileInfo fileInfo = fileService.getFileBySeq(fileSeq);
		log.info("Download filename: {}", fileInfo.getFileOrgNm());
		
		Path path = Paths.get(fileInfo.getFileRealPath()).resolve(fileInfo.getFileRealNm());
		Resource resource = fileService.getFileResource(path);
		
		File file = resource.getFile();
		
		MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
		
		String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
		if(contentType != null) {
			mediaType = MediaType.parseMediaType(contentType);
		}
		log.info("Content Type: {}", mediaType);
		
		String download = URLEncoder.encode(fileInfo.getFileOrgNm(), "UTF-8").replaceAll("\\+", "%20");
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.TRANSFER_ENCODING, "binary");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download + "\"");
        
        return ResponseEntity.ok()
        		.cacheControl(CacheControl.noCache())
                .contentType(mediaType)
                .headers(headers)
                .body(resource);
	}
	

}
