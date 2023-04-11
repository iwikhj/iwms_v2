package com.iwi.iwms.api.file.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;

public interface FileService {

	List<UploadFileInfo> listFileByRef(UploadFile uploadFile);
	
	void insertFiles(List<MultipartFile> multipartFiles, UploadFile uploadFile);
	
	void deleteFiles(List<UploadFileInfo> uploadedFilesInfo, List<Long> excludeFilesSeq);
	
	File upload(MultipartFile multipartFile, Path path);
	
	UploadFileInfo getFileBySeq(long fileSeq);
	
	Resource getFileResource(Path path);
	
	void deleteFolder(Path path);
}
