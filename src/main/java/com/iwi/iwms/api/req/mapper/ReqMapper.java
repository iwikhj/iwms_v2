package com.iwi.iwms.api.req.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqHis;
import com.iwi.iwms.api.req.domain.ReqInfo;

@Mapper
public interface ReqMapper {

	List<ReqInfo> listReq(Map<String, Object> map);
	
	int countReq(Map<String, Object> map);
	
	ReqInfo getReqBySeq(long reqSeq);
	
	void insertReq(Req req);
	
	int updateReq(Req req);
	
	int deleteReq(Req req);
	
	void insertReqHis(ReqHis reqHis);
}
