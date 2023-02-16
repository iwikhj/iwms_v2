package com.iwi.iwms.api.comp.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.comp.domain.Comp;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.domain.Position;
import com.iwi.iwms.api.comp.domain.PositionInfo;

public interface CompService {

	List<CompInfo> listComp(Map<String, Object> map);
	
	int countComp(Map<String, Object> map);

	CompInfo getCompBySeq(long compSeq);
	
	void insertComp(Comp comp);
	
	int updateComp(Comp comp);
	
	int deleteComp(Comp comp);
	
	List<PositionInfo> listPosition(Map<String, Object> map);

	void insertPosition(Position position);
	
	int updatePosition(Position position);
	
	int deletePosition(Position position);
}
