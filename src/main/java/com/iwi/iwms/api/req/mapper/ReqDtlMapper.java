package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.His;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;

@Mapper
public interface ReqDtlMapper {

	int countReqDtlByReqSeq(Map<String, Object> map);
	
	ReqDtlInfo getReqDtlByReqSeq(Map<String, Object> map);
	
	ReqDtlInfo getReqDtlBySeq(Map<String, Object> map);
	
	void insertReqDtl(ReqDtl reqDtl);
	
	int updateReqDtl(ReqDtl reqDtl);
	
	int deleteReqDtl(ReqDtl reqDtl);
	
	int updateReqDtlStatInProgress(ReqDtl reqDtl);
	
	int updateReqDtlStatProcessed(ReqDtl reqDtl);
	
	int updateReqDtlStatInspectionCompleted(ReqDtl reqDtl);
	
	int updateReqDtlStatCancel(ReqDtl reqDtl);
	
	void insertReqDtlHis(His his);

	
}
