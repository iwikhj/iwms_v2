package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;

@Mapper
public interface CompMapper {

	List<CompInfo> listComp(Map<String, Object> map);
	
	int countComp(Map<String, Object> map);
	
	CompInfo getCompBySeq(long compSeq);
	
	void insertComp(Comp comp);
	
	int updateComp(Comp comp);
	
	int deleteComp(Comp comp);
}
