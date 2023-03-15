package com.iwi.iwms.api.req.service;

import java.util.Map;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;


public interface ReqDtlService {

	ReqDtlInfo getReqDtlByReqAndDtlSeq(Map<String, Object> map);
	
	ReqDtlInfo getReqDtlBySeq(long reqSeq);

	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	int updateReqDtlStatByInProgress(ReqDtl reqDtl);
	
	int updateReqDtlStatByProcessed(ReqDtl reqDtl);
	
	int updateReqDtlStatByInspectionCompleted(ReqDtl reqDtl);
	
	int updateReqDtlStatByCancel(ReqDtl reqDtl);
}
