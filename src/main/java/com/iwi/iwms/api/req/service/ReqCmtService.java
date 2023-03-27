package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;

public interface ReqCmtService {

	CmtInfo getReqCmtBySeq(long cmtSeq, long loginUserSeq);
	
	CmtInfo insertReqCmt(Cmt cmt);
	
	CmtInfo updateReqCmt(Cmt cmt);

	int deleteReqCmt(Cmt cmt);
	
}
