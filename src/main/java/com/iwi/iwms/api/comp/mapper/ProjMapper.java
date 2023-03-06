package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUser;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;

@Mapper
public interface ProjMapper {

	List<ProjInfo> listProj(Map<String, Object> map);
	
	int countProj(Map<String, Object> map);
	
	ProjInfo getProjBySeq(long projSeq);
	
	void insertProj(Proj proj);
	
	int updateProj(Proj proj);
	
	int deleteProj(Proj proj);
	
	List<ProjUserInfo> listCustProjUser(long projSeq);
	
	List<ProjUserInfo> listPerfProjUser(long projSeq);
	
	int updateProjUser(List<ProjUser> projUserList);
	
	int deleteProjUser(long projSeq);
}
