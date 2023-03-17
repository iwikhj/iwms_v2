package com.iwi.iwms.api.req.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqCmt;
import com.iwi.iwms.api.req.domain.ReqCmtInfo;

@Mapper
public interface ReqCmtMapper {
	
	ReqCmtInfo getReqCmtBySeq(Map<String, Object> map);

	void insertReqCmt(ReqCmt reqCmt);
	
	int updateReqCmt(ReqCmt reqCmt);
	
	int deleteReqCmt(ReqCmt reqCmt);

}
