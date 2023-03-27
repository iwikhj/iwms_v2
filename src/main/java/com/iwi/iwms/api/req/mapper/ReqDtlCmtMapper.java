package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;

@Mapper
public interface ReqDtlCmtMapper {
	
	CmtInfo getReqDtlCmtBySeq(Map<String, Object> map);

	void insertReqDtlCmt(Cmt cmt);
	
	int updateReqDtlCmt(Cmt cmt);
	
	int deleteReqDtlCmt(Cmt cmt);

}
