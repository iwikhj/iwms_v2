package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqCmt;
import com.iwi.iwms.api.req.domain.ReqCmtInfo;

public interface ReqCmtService {

	ReqCmtInfo getReqCmtBySeq(long reqCmtSeq);
	
	void insertReqCmt(ReqCmt reqCmt);
	
	int updateReqCmt(ReqCmt reqCmt);

	int deleteReqCmt(ReqCmt reqCmt);
	
}
