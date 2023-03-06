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
		return compMapper.listComp(map);
	}

	@Override
	public int countComp(Map<String, Object> map) {
		return compMapper.countComp(map);
	}

	@Override
	public CompInfo getCompBySeq(long compSeq) {
		return Optional.ofNullable(compMapper.getCompBySeq(compSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
	}

	@Override
	public void insertComp(Comp comp) {
		compMapper.insertComp(comp);
	}

	@Override
	public int updateComp(Comp comp) {
		Optional.ofNullable(compMapper.getCompBySeq(comp.getCompSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
		
		return compMapper.updateComp(comp);
	}

	@Override
	public int deleteComp(Comp comp) {
		Optional.ofNullable(compMapper.getCompBySeq(comp.getCompSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
		
		return compMapper.deleteComp(comp);
	}

	@Override
	public List<PositionInfo> listPosition(Map<String, Object> map) {
		return positionMapper.listPosition(map);
	}

	@Override
	public void insertPosition(Position position) {
		Optional.ofNullable(compMapper.getCompBySeq(position.getCompSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소속을 찾을 수 없습니다."));
		
		positionMapper.insertPosition(position);
	}

	@Override
	public int updatePosition(Position position) {
		Optional.ofNullable(positionMapper.getPositionBySeq(position))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직급을 찾을 수 없습니다."));
		
		return positionMapper.updatePosition(position);
	}

	@Override
	public int deletePosition(Position position) {
		Optional.ofNullable(positionMapper.getPositionBySeq(position))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "직급을 찾을 수 없습니다."));
		
		return positionMapper.deletePosition(position);
	}

}
