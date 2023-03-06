package com.iwi.iwms.api.comp.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;
import com.iwi.iwms.api.comp.domain.ProjUserList;

public interface ProjService {

	List<ProjInfo> listProj(Map<String, Object> map);
	
	int countProj(Map<String, Object> map);

	ProjInfo getProjBySeq(long projSeq);
	
	void insertProj(Proj proj);
	
	int updateProj(Proj proj);
	
	int deleteProj(Proj proj);
	
	List<ProjUserInfo> listCustProjUser(long projSeq);
	
	List<ProjUserInfo> listPerfProjUser(long projSeq);

	int updateProjUser(ProjUserList projUserList);
}
