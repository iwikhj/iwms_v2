package com.iwi.iwms.api.file.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;

@Mapper
public interface FileMapper {

	List<UploadFileInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	UploadFileInfo findBySeq(long fileSeq);
	
	void save(UploadFile uploadFile);
	
	int update(UploadFile uploadFile);
	
	int delete(UploadFile uploadFile);
	
	int updateOrderNum(UploadFile uploadFile);
}
