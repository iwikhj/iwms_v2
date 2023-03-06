package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;

@Mapper
public interface SiteMapper {

	List<SiteInfo> listSite(Map<String, Object> map);
	
	int countSite(Map<String, Object> map);
	
	SiteInfo getSiteBySeq(Site site);
	
	void insertSite(Site site);
	
	int updateSite(Site site);
	
	int deleteSite(Site site);
}
