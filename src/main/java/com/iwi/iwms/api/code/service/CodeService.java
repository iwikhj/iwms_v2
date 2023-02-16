package com.iwi.iwms.api.code.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;

public interface CodeService {

	List<CodeInfo> listCode(Map<String, Object> map);

	void insertCode(Code code);
	
	int updateCode(Code code);
	
	int deleteCode(Code code);
}
