package com.iwi.iwms.api.req.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqCmt;
import com.iwi.iwms.api.req.domain.ReqCmtInfo;

@Mapper
public interface ReqCmtMapper {
	
	List<ReqCmtInfo> listReqCmtByReqSeq(long reqSeq);
	
	ReqCmtInfo getReqCmtBySeq(long reqCmtSeq);

	void insertReqCmt(ReqCmt reqCmt);
	
	int updateReqCmt(ReqCmt reqCmt);
	
	int deleteReqCmt(ReqCmt reqCmt);

}
