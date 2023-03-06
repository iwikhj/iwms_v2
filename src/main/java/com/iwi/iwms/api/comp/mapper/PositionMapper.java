package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Position;
import com.iwi.iwms.api.comp.domain.PositionInfo;

@Mapper
public interface PositionMapper {

	List<PositionInfo> listPosition(Map<String, Object> map);
	
	PositionInfo getPositionBySeq(Position position);
	
	void insertPosition(Position position);
	
	int updatePosition(Position position);
	
	int deletePosition(Position position);
}
