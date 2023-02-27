package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;


public interface ReqDtlService {

	ReqDtlInfo getReqDtlBySeq(long reqDtlSeq);
	
	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	ReqDtlCmtInfo getReqDtlCmtBySeq(long reqDtlCmtSeq);
	
	void insertReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int updateReqDtlCmt(ReqDtlCmt reqDtlCmt);

	int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
}
