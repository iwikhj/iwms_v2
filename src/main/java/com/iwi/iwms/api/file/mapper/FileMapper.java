package com.iwi.iwms.api.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;

@Mapper
public interface FileMapper {

	List<UploadFileInfo> listFileByRef(UploadFile uploadFile);
	
	UploadFileInfo getFileBySeq(long fileSeq);
	
	void insertFile(UploadFile uploadFile);
	
	int deleteFile(long fileSeq);
	
	int updateOrderNum(UploadFile uploadFile);
}
