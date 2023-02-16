package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;

@Mapper
public interface ProjMapper {

	List<ProjInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	ProjInfo findBySeq(Proj proj);
	
	void save(Proj proj);
	
	int update(Proj proj);
	
	int delete(Proj proj);
}
