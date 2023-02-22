package com.iwi.iwms.api.file.service;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.filestorage.FileStorageResponse;

public interface FileService {

	FileStorageResponse upload(MultipartFile multipartFile, Path path);
	
	UploadFileInfo getFileBySeq(long fileSeq);
	
	Resource getFileResource(Path path);
}
