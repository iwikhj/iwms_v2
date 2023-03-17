package com.iwi.iwms.api.comp.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;

public interface CompService {

	List<CompInfo> listComp(Map<String, Object> map);
	
	int countComp(Map<String, Object> map);

	CompInfo getCompBySeq(long compSeq, long loginUserSeq);
	
	void insertComp(Comp comp);
	
	int updateComp(Comp comp);
	
	int deleteComp(Comp comp);
	
	List<DeptInfo> listDeptByCompSeq(Map<String, Object> map);
	
	DeptInfo getDeptBySeq(long deptSeq, long loginUserSeq);

	void insertDept(Dept dept);
	
	int updateDept(Dept dept);
	
	int deleteDept(Dept dept);
}
