package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;

public interface ReqDtlService {

	ReqDtlInfo getReqDtlByReqSeq(long reqSeq, long loginUserSeq);
	
	ReqDtlInfo getReqDtlBySeq(long reqSeq, long reqDtlSeq, long loginUserSeq);

	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	void updateReqDtlStat(ReqDtl reqDtl);
}
