package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlHis;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;

@Mapper
public interface ReqDtlMapper {

	ReqDtlInfo getReqDtlByReqAndDtlSeq(Map<String, Object> map);
	
	ReqDtlInfo getReqDtlBySeq(long reqSeq);
	
	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	int updateReqDtlStatByInProgress(ReqDtl reqDtl);
	
	int updateReqDtlStatByProcessed(ReqDtl reqDtl);
	
	int updateReqDtlStatByInspectionCompleted(ReqDtl reqDtl);
	
	int updateReqDtlStatByCancel(ReqDtl reqDtl);
	
	void insertReqDtlHis(ReqDtlHis reqDtlHis);

	
}
