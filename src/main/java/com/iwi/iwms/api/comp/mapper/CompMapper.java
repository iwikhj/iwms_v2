package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;

@Mapper
public interface CompMapper {

	List<CompInfo> listComp(Map<String, Object> map);
	
	int countComp(Map<String, Object> map);
	
	CompInfo getCompBySeq(Map<String, Object> map);
	
	void insertComp(Comp comp);
	
	int updateComp(Comp comp);
	
	int deleteComp(Comp comp);
	
	List<DeptInfo> listDeptByCompSeq(Map<String, Object> map);
	
	DeptInfo getDeptBySeq(Map<String, Object> map);
	
	void insertDept(Dept dept);
	
	int updateDept(Dept dept);
	
	int deleteDept(Dept dept);
}
