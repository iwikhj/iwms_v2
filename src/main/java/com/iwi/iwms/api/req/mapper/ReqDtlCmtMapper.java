package com.iwi.iwms.api.req.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;

@Mapper
public interface ReqDtlCmtMapper {
	
	List<ReqDtlCmtInfo> listReqDtlCmtByReqDtlSeq(long reqDtlSeq);
	
	ReqDtlCmtInfo getReqDtlCmtBySeq(long reqDtlCmtSeq);

	void insertReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int updateReqDtlCmt(ReqDtlCmt reqDtlCmt);
	
	int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt);

}
