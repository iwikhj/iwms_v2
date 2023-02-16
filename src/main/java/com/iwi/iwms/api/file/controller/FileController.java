package com.iwi.iwms.api.file.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.common.response.ApiResponse;
import com.iwi.iwms.api.common.response.ApiListResponse;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.utils.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "File", description = "IWMS 파일 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/file")
public class FileController {
	
    private final FileService fileService;
    
	@Operation(summary = "파일 목록", description = "파일 목록")
	@GetMapping(value = "")
	public ResponseEntity<ApiListResponse<List<UploadFileInfo>>> listUploadFile(HttpServletRequest request
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit
			, @RequestParam(value = "search", required = false) String search
			, @RequestParam(value = "startDate", required = false) String startDate
			, @RequestParam(value = "endDate", required = false) String endDate) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("pagination", new Pagination(page, limit, fileService.countUploadFile(map)));
		
		List<UploadFileInfo> uploadFileList = fileService.listUploadFile(map);
		
		return ResponseEntity.ok(ApiListResponse.<List<UploadFileInfo>>builder()
				.request(request)
				.data(uploadFileList)
				.query(map)
				.build());
	}
	
    @Operation(summary = "파일 정보", description = "FILE SEQ와 일치하는 파일 정보")
    @GetMapping(value = "/{fileSeq}")
    public ResponseEntity<ApiResponse<UploadFileInfo>> getUploadFileInfoBySeq(HttpServletRequest request
    		, @PathVariable long fileSeq) {
    	
    	UploadFileInfo uploadFile = fileService.getUploadFileInfoBySeq(fileSeq);
    	
		return ResponseEntity.ok(ApiResponse.<UploadFileInfo>builder()
				.request(request)
				.data(uploadFile)
				.build());
    }
    
    @Operation(summary = "다중 파일 업로드", description = "다중 파일을 업로드합니다.")
    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiListResponse<List<UploadFileInfo>>> fileUpload(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @ModelAttribute @Valid UploadFile uploadFile) {
    	
    	List<UploadFileInfo> uploadFileInfoList = new ArrayList<>();
    	
    	for(int i = 0; i < uploadFile.getMultipartFiles().size(); i++) {
    		MultipartFile multipartFile = uploadFile.getMultipartFiles().get(i);
    		try {
    			uploadFileInfoList.add(fileService.insertUploadFile(uploadFile.build(uploadFile, multipartFile, loginUserInfo)));
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
		return ResponseEntity.ok(ApiListResponse.<List<UploadFileInfo>>builder()
				.request(request)
				.data(uploadFileInfoList)
				.build());
    }
    
    @Operation(summary = "파일 삭제", description = "FILE SEQ와 일치하는 파일 정보 및 파일 삭제")
	@DeleteMapping(value = "/{fileSeq}")
	public ResponseEntity<ApiResponse<Boolean>> deleteUploadFile(HttpServletRequest request
			, @Parameter(hidden = true) LoginUserInfo loginUserInfo		
			, @PathVariable long fileSeq
			, @Parameter(hidden = true) UploadFile uploadFile) {
    	
    	boolean result = fileService.deleteUploadFile(uploadFile.build(uploadFile, null, loginUserInfo)) > 0 ? true : false;

		return ResponseEntity.ok(ApiResponse.<Boolean>builder()
				.request(request)
				.data(result)
				.build());
	}
    
	@Operation(summary = "파일 다운로드", description = "파일 다운로드")
	@GetMapping(value = "/{fileSeq}/download")
	public ResponseEntity<Resource> download(HttpServletRequest request
			, @PathVariable long fileSeq) throws IOException {
		
		UploadFileInfo fileInfo = fileService.getUploadFileInfoBySeq(fileSeq);
		log.info("Upload File Info: {}", fileInfo);
		
		Path path = Paths.get(fileInfo.getFileRealPath()).resolve(fileInfo.getFileRealNm());
		Resource resource = fileService.getUploadFileResource(path);
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
