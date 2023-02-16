package com.iwi.iwms.api.file.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;

public interface FileService {

	List<UploadFileInfo> listUploadFile(Map<String, Object> map);
	
	int countUploadFile(Map<String, Object> map);

	UploadFileInfo getUploadFileInfoBySeq(long fileSeq);
	
	UploadFileInfo insertUploadFile(UploadFile uploadFile);
	
	int deleteUploadFile(UploadFile uploadFile);
	
	Resource getUploadFileResource(Path path);
}
