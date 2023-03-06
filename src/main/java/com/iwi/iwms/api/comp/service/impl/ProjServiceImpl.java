package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUserList;
import com.iwi.iwms.api.comp.mapper.ProjMapper;
import com.iwi.iwms.api.comp.service.ProjService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjServiceImpl implements ProjService {

	private final ProjMapper projMapper;
	
	@Override
	public List<ProjInfo> listProj(Map<String, Object> map) {
		return projMapper.listProj(map);
	}

	@Override
	public int countProj(Map<String, Object> map) {
		return projMapper.countProj(map);
	}

	@Override
	public ProjInfo getProjBySeq(long projSeq) {
		return Optional.ofNullable(projMapper.getProjBySeq(projSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
	}

	@Override
	public void insertProj(Proj proj) {
		projMapper.insertProj(proj);
	}

	@Override
	public int updateProj(Proj proj) {
		Optional.ofNullable(projMapper.getProjBySeq(proj.getProjSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
		
		return projMapper.updateProj(proj);
	}

	@Override
	public int deleteProj(Proj proj) {
		Optional.ofNullable(projMapper.getProjBySeq(proj.getProjSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
		
		return projMapper.deleteProj(proj);
	}

	@Override
	public Map<String, Object> listProjUser(long projSeq) {
		Optional.ofNullable(projMapper.getProjBySeq(projSeq))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));

		return projMapper.listProjUser(projSeq);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateProjUser(ProjUserList projUserList) {
		Optional.ofNullable(projMapper.getProjBySeq(projUserList.getProjSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
		
		projMapper.deleteProjUser(projUserList.getProjSeq());
		return projMapper.updateProjUser(projUserList.getUsers());
	}

}
