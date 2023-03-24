package com.iwi.iwms.api.code.service;

import java.util.List;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;

public interface CodeService {

	List<CodeInfo> listCodeByUpCode(String codeCd);
	
	void insertCode(Code code);
	
	int updateCode(Code code);
	
	int deleteCode(Code code);
}
