package com.iwi.iwms.api.req.service;

import com.iwi.iwms.api.req.domain.ReqDtlUser;

public interface ReqDtlUserService {

	void insertReqDtlUser(ReqDtlUser reqDtlUser);
	
	int updateReqDtlUser(ReqDtlUser reqDtlUser);

	int deleteReqDtlUser(ReqDtlUser reqDtlUser);
}
