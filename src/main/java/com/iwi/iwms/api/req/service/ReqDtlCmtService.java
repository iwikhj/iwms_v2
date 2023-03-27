package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;

public interface ReqDtlCmtService {

	CmtInfo getReqDtlCmtBySeq(long cmtSeq, long loginUserSeq);
	
	CmtInfo insertReqDtlCmt(Cmt cmt);
	
	CmtInfo updateReqDtlCmt(Cmt cmt);

	int deleteReqDtlCmt(Cmt cmt);
	
}
