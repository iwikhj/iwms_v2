package com.iwi.iwms.api.code.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;

@Mapper
public interface CodeMapper {

	List<CodeInfo> findAll(Map<String, Object> map);
	
	void save(Code code);
	
	int update(Code code);
	
	int delete(Code code);
}
