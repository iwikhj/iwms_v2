package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;

@Mapper
public interface ReqCmtMapper {
	
	CmtInfo getReqCmtBySeq(Map<String, Object> map);

	void insertReqCmt(Cmt cmt);
	
	int updateReqCmt(Cmt cmt);
	
	int deleteReqCmt(Cmt cmt);

}
