package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Position;
import com.iwi.iwms.api.comp.domain.PositionInfo;

@Mapper
public interface PositionMapper {

	List<PositionInfo> findAll(Map<String, Object> map);
	
	PositionInfo findBySeq(Position position);
	
	void save(Position position);
	
	int update(Position position);
	
	int delete(Position position);
}
