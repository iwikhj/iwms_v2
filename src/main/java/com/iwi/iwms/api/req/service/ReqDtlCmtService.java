package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;

public interface ReqDtlCmtService {

	ReqDtlCmtInfo getReqDtlCmtBySeq(long reqDtlCmtSeq, long loginUserSeq);
	
	void insertReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int updateReqDtlCmt(ReqDtlCmt reqDtlCmt);

	int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
}
