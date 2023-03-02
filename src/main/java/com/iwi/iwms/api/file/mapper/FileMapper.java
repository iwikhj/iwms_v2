package com.iwi.iwms.api.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;

@Mapper
public interface FileMapper {

	List<UploadFileInfo> findAllByRef(UploadFile uploadFile);
	
	UploadFileInfo findBySeq(long fileSeq);
	
	void save(UploadFile uploadFile);
	
	int delete(long fileSeq);
	
	int updateOrderNum(UploadFile uploadFile);
}
