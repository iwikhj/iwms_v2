package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUser;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;
import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;

@Mapper
public interface ProjMapper {

	List<ProjInfo> listProj(Map<String, Object> map);
	
	int countProj(Map<String, Object> map);
	
	ProjInfo getProjBySeq(Map<String, Object> map);
	
	void insertProj(Proj proj);
	
	int updateProj(Proj proj);
	
	int deleteProj(Proj proj);
	
	List<ProjUserInfo> listCustProjUser(long projSeq);
	
	List<ProjUserInfo> listPerfProjUser(long projSeq);
	
	int updateProjUser(ProjUser projUser);
	
	int deleteProjUser(ProjUser projUser);
	
	List<SiteInfo> listSite(Map<String, Object> map);
	
	int countSite(Map<String, Object> map);
	
	SiteInfo getSiteBySeq(Map<String, Object> map);
	
	void insertSite(Site site);
	
	int updateSite(Site site);
	
	int deleteSite(Site site);
}
