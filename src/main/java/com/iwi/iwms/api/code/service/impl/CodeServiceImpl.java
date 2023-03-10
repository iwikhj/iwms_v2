package com.iwi.iwms.api.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.iwi.iwms.api.code.domain.Code;
import com.iwi.iwms.api.code.domain.CodeInfo;
import com.iwi.iwms.api.code.mapper.CodeMapper;
import com.iwi.iwms.api.code.service.CodeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {

	private final CodeMapper codeMapper;
	
	@Override
	public List<CodeInfo> listCode(Map<String, Object> map) {
		return codeMapper.listCode(map);
	}

	@Override
	public void insertCode(Code code) {
		codeMapper.insertCode(code);
	}

	@Override
	public int updateCode(Code code) {
		return codeMapper.updateCode(code);
	}

	@Override
	public int deleteCode(Code code) {
		return codeMapper.deleteCode(code);
	}

}
