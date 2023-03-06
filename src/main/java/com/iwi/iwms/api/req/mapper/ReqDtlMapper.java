package com.iwi.iwms.api.req.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;

@Mapper
public interface ReqDtlMapper {

	ReqDtlInfo getReqDtlBySeq(long reqSeq);
	
	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	void insertReqDtlHis(ReqDtl reqDtl);

}
