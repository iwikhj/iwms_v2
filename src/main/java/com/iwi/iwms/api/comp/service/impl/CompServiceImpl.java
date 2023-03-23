package com.iwi.iwms.api.comp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.comp.mapper.CompMapper;
import com.iwi.iwms.api.comp.service.CompService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompServiceImpl implements CompService {

	private final CompMapper compMapper;
	
	@Override
	public List<CompInfo> listComp(Map<String, Object> map) {
		return compMapper.listComp(map);
	}

	@Override
	public int countComp(Map<String, Object> map) {
		return compMapper.countComp(map);
	}

	@Override
	public CompInfo getCompBySeq(long compSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("compSeq", compSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(compMapper.getCompBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "소속을 찾을 수 없습니다."));
	}

	@Override
	public void insertComp(Comp comp) {
		compMapper.insertComp(comp);
	}

	@Override
	public int updateComp(Comp comp) {
		this.getCompBySeq(comp.getCompSeq(), comp.getLoginUserSeq());
		return compMapper.updateComp(comp);
	}

	@Override
	public int deleteComp(Comp comp) {
		this.getCompBySeq(comp.getCompSeq(), comp.getLoginUserSeq());		
		return compMapper.deleteComp(comp);
	}

	@Override
	public List<DeptInfo> listDept(Map<String, Object> map) {
		return compMapper.listDept(map);
	}

	@Override
	public DeptInfo getDeptBySeq(long deptSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("deptSeq", deptSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(compMapper.getDeptBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "부서를 찾을 수 없습니다."));

	}

	@Override
	public void insertDept(Dept dept) {
		compMapper.insertDept(dept);
	}

	@Override
	public int updateDept(Dept dept) {
		this.getCompBySeq(dept.getCompSeq(), dept.getLoginUserSeq());		
		return compMapper.updateDept(dept);
	}

	@Override
	public int deleteDept(Dept dept) {
		this.getCompBySeq(dept.getCompSeq(), dept.getLoginUserSeq());		
		return compMapper.deleteDept(dept);
	}

}
