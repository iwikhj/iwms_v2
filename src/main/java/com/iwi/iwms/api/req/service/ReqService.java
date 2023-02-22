package com.iwi.iwms.api.req.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;

public interface ReqService {

	List<ReqInfo> listReq(Map<String, Object> map);
	
	int countReq(Map<String, Object> map);

	ReqInfo getReqBySeq(Req req);
	
	void insertReq(Req req);
	
	int updateReq(Req req);
	
	int deleteReq(Req req);
}
