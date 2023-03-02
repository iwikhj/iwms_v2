package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtl;


public interface ReqDtlService {

	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
}
