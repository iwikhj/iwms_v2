package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Position;
import com.iwi.iwms.api.comp.domain.PositionInfo;
import com.iwi.iwms.api.comp.mapper.CompMapper;
import com.iwi.iwms.api.comp.mapper.PositionMapper;
import com.iwi.iwms.api.comp.service.CompService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompServiceImpl implements CompService {

	private final CompMapper compMapper;
	
	private final PositionMapper positionMapper;
	
	@Override
	public List<CompInfo> listComp(Map<String, Object> map) {
		return compMapper.findAll(map);
	}

	@Override
	public int countComp(Map<String, Object> map) {
		return compMapper.count(map);
	}

	@Override
	public CompInfo getCompBySeq(long compSeq) {
		return Optional.ofNullable(compMapper.findBySeq(compSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
	}

	@Override
	public void insertComp(Comp comp) {
		compMapper.save(comp);
	}

	@Override
	public int updateComp(Comp comp) {
		Optional.ofNullable(compMapper.findBySeq(comp.getCompSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
		
		return compMapper.update(comp);
	}

	@Override
	public int deleteComp(Comp comp) {
		Optional.ofNullable(compMapper.findBySeq(comp.getCompSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
		
		return compMapper.delete(comp);
	}

	@Override
	public List<PositionInfo> listPosition(Map<String, Object> map) {
		return positionMapper.findAll(map);
	}

	@Override
	public void insertPosition(Position position) {
		positionMapper.save(position);
	}

	@Override
	public int updatePosition(Position position) {
		Optional.ofNullable(positionMapper.findBySeq(position))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직급을 찾을 수 없습니다."));
		
		return positionMapper.update(position);
	}

	@Override
	public int deletePosition(Position position) {
		Optional.ofNullable(positionMapper.findBySeq(position))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직급을 찾을 수 없습니다."));
		
		return positionMapper.delete(position);
	}

}
