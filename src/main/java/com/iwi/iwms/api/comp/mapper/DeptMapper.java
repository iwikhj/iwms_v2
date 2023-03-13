package com.iwi.iwms.api.comp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Dept;
import com.iwi.iwms.api.comp.domain.DeptInfo;

@Mapper
public interface DeptMapper {

	List<DeptInfo> listDeptByCompSeq(long compSeq);
	
	DeptInfo getDeptBySeq(long deptSeq);
	
	void insertDept(Dept dept);
	
	int updateDept(Dept dept);
	
	int deleteDept(Dept dept);
}
