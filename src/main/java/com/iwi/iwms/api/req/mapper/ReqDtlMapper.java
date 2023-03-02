package com.iwi.iwms.api.req.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;

@Mapper
public interface ReqDtlMapper {

	ReqDtlInfo findBySeq(long reqSeq);
	
	void save(ReqDtl reqDtl);
	
	int update(ReqDtl reqDtl);
	
	int delete(ReqDtl reqDtl);
	
	void saveHistory(ReqDtl reqDtl);

}
