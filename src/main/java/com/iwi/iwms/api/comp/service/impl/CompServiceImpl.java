package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.mapper.CompMapper;
import com.iwi.iwms.api.comp.mapper.DeptMapper;
import com.iwi.iwms.api.comp.service.CompService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompServiceImpl implements CompService {

	private final CompMapper compMapper;
	
	private final DeptMapper deptMapper;
	
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
		this.getCompBySeq(comp.getCompSeq());
		return compMapper.updateComp(comp);
	}

	@Override
	public int deleteComp(Comp comp) {
		this.getCompBySeq(comp.getCompSeq());		
		return compMapper.deleteComp(comp);
	}

	@Override
	public List<DeptInfo> listDeptByCompSeq(long compSeq) {
		return deptMapper.listDeptByCompSeq(compSeq);
	}

	@Override
	public void insertDept(Dept dept) {
		this.getCompBySeq(dept.getCompSeq());		
		deptMapper.insertDept(dept);
	}

	@Override
	public int updateDept(Dept dept) {
		this.getCompBySeq(dept.getCompSeq());		
		return deptMapper.updateDept(dept);
	}

	@Override
	public int deleteDept(Dept dept) {
		this.getCompBySeq(dept.getCompSeq());		
		return deptMapper.deleteDept(dept);
	}

}
