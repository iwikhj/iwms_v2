package com.iwi.iwms.api.req.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.req.domain.ReqDtlUser;
import com.iwi.iwms.api.req.domain.ReqDtlUserInfo;

@Mapper
public interface ReqDtlUserMapper {
	
	List<ReqDtlUserInfo> listReqDtlUserByReqDtlSeq(long reqDtlSeq);
	
	ReqDtlUserInfo getReqDtlUserBySeq(long reqDtlUserSeq);

	void insertReqDtlUser(ReqDtlUser reqDtlUser);
	
	int updateReqDtlUser(ReqDtlUser reqDtlUser);
	
	int deleteReqDtlUser(ReqDtlUser reqDtlUser);
	
	void insertReqDtlUserCmt(ReqDtlUser reqDtlUser);
	
	int updateReqDtlUserCmt(ReqDtlUser reqDtlUser);

}
