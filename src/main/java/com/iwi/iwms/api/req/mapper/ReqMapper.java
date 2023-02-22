package com.iwi.iwms.api.req.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;

@Mapper
public interface ReqMapper {

	List<ReqInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	ReqInfo findBySeq(Req req);
	
	void save(Req req);
	
	int update(Req req);
	
	int delete(Req req);
}
