package com.iwi.iwms.api.code.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;

@Mapper
public interface CodeMapper {

	List<CodeInfo> listCode(Map<String, Object> map);
	
	void insertCode(Code code);
	
	int updateCode(Code code);
	
	int deleteCode(Code code);
}
