package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.mapper.ProjMapper;
import com.iwi.iwms.api.comp.service.ProjService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjServiceImpl implements ProjService {

	private final ProjMapper projMapper;
	
	@Override
	public List<ProjInfo> listProj(Map<String, Object> map) {
		return projMapper.findAll(map);
	}

	@Override
	public int countProj(Map<String, Object> map) {
		return projMapper.count(map);
	}

	@Override
	public ProjInfo getProjBySeq(Proj proj) {
		return Optional.ofNullable(projMapper.findBySeq(proj))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
	}

	@Override
	public void insertProj(Proj proj) {
		projMapper.save(proj);
	}

	@Override
	public int updateProj(Proj proj) {
		Optional.ofNullable(projMapper.findBySeq(proj))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
		
		return projMapper.update(proj);
	}

	@Override
	public int deleteProj(Proj proj) {
		Optional.ofNullable(projMapper.findBySeq(proj))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
		
		return projMapper.delete(proj);
	}

}
