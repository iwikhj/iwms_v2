package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;

@Mapper
public interface CompMapper {

	List<CompInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	CompInfo findBySeq(long compSeq);
	
	void save(Comp comp);
	
	int update(Comp comp);
	
	int delete(Comp comp);
}
