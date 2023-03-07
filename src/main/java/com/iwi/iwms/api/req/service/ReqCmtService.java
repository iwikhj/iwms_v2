package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqCmt;

public interface ReqCmtService {

	void insertReqCmt(ReqCmt reqCmt);
	
	int updateReqCmt(ReqCmt reqCmt);

	int deleteReqCmt(ReqCmt reqCmt);
	
}
