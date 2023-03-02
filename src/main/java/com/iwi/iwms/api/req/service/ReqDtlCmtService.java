package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtlCmt;

public interface ReqDtlCmtService {

	void insertReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int updateReqDtlCmt(ReqDtlCmt reqDtlCmt);

	int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
}
