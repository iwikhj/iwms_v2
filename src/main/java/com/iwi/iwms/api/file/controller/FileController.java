package com.iwi.iwms.api.file.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
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
import com.iwi.iwms.filestorage.FileStorageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "File", description = "IWMS 파일 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.path}/files")
public class FileController {
	
	private final FileService fileService;
	
    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(value = "/upload")
    public ResponseEntity<ApiResponse<FileStorageResponse>> upload(HttpServletRequest request
			//, @Parameter(hidden = true) loginInfo loginInfo	
			, @RequestParam(value = "file", required = true) MultipartFile multipartFile) throws IOException {
    	
    	String ms = String.valueOf(System.currentTimeMillis()) ;
    	String firstPath = ms.substring(0, 3);
    	String secondPath = ms.substring(3, 6);
    	
		Path path = Paths.get(firstPath).resolve(secondPath);
		File file = fileService.upload(multipartFile, path);
		
      	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return ResponseEntity.ok(ApiResponse.<FileStorageResponse>builder()
				.data(FileStorageResponse.builder()
						.filename(multipartFile.getOriginalFilename())
						.url(request.getRequestURL().toString().replaceFirst("upload", "link").concat("/" + path.toString().replace("\\", "/") + "/" + file.getName()))
						.type(Files.probeContentType(file.toPath()))
						.size(file.length())	
						.lastModified(sdf.format(file.lastModified()))
						.build())
				.build());
    }
    
	@Operation(hidden = true, summary = "파일 링크", description = "파일 링크")
	@GetMapping(value = "/link/{path1}/{path2}/{filename:.+}")
	public ResponseEntity<Resource> link(HttpServletRequest request
			//, @Parameter(hidden = true) loginInfo loginInfo	
			, @PathVariable String path1
			, @PathVariable String path2
			, @PathVariable String filename) throws IOException {
		
		Path path = Paths.get(path1).resolve(path2).resolve(filename);
		Resource resource = fileService.getFileResource(path);
		
		MediaType contentType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
		log.info("Content Type: {}", contentType);
		
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        
        return ResponseEntity.ok()
        		.cacheControl(CacheControl.noCache())
                .contentType(contentType)
                .headers(headers)
                .body(resource);
	}
    
	@Operation(hidden = true, summary = "파일 다운로드", description = "파일 다운로드")
	@GetMapping(value = "/download/{fileSeq}")
	public ResponseEntity<Resource> download(HttpServletRequest request
			//, @Parameter(hidden = true) loginInfo loginInfo	
			, @PathVariable long fileSeq) throws IOException {
		
		UploadFileInfo fileInfo = fileService.getFileBySeq(fileSeq);
		
		Path path = Paths.get(fileInfo.getFileRealPath()).resolve(fileInfo.getFileRealNm());
		Resource resource = fileService.getFileResource(path);
		
		MediaType contentType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
		log.info("Content Type: {}", contentType);
		
		String download = URLEncoder.encode(fileInfo.getFileOrgNm(), "UTF-8").replaceAll("\\+", "%20");
		log.info("Download filename: {}", fileInfo.getFileOrgNm());
		
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download + "\"");
        
        return ResponseEntity.ok()
        		.cacheControl(CacheControl.noCache())
                .contentType(contentType)
                .headers(headers)
                .body(resource);
	}
	

}
