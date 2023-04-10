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
	
	void insertAttachFiles(List<MultipartFile> multipartFiles, UploadFile uploadFile);
	
	void deleteAttachFiles(List<UploadFileInfo> oldAttachedFiles, List<Long> currentAttachedFilesSeq);
	
	void deleteAttachAll(List<UploadFileInfo> attachedFiles);
	
	File upload(MultipartFile multipartFile, Path path);
	
	UploadFileInfo getFileBySeq(long fileSeq);
	
	Resource getFileResource(Path path);
	
	void deletePath(Path path);
}
