package com.iwi.iwms.api.req.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;

@Mapper
public interface ReqDtlCmtMapper {
	
	List<ReqDtlCmtInfo> findAllByReqDtlSeq(long reqDtlSeq);
	
	ReqDtlCmtInfo findBySeq(long reqDtlCmtSeq);

	void save(ReqDtlCmt reqDtlCmt);
	
	int update(ReqDtlCmt reqDtlCmt);
	
	int delete(ReqDtlCmt reqDtlCmt);

}
