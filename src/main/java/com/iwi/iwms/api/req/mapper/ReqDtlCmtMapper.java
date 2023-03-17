package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;

@Mapper
public interface ReqDtlCmtMapper {
	
	ReqDtlCmtInfo getReqDtlCmtBySeq(Map<String, Object> map);

	void insertReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int updateReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt);

}
