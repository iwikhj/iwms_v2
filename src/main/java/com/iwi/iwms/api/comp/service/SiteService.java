package com.iwi.iwms.api.comp.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;

public interface SiteService {

	List<SiteInfo> listSite(Map<String, Object> map);
	
	int countSite(Map<String, Object> map);

	SiteInfo getSiteBySeq(long siteSeq);
	
	void insertSite(Site site);
	
	int updateSite(Site site);
	
	int deleteSite(Site site);
}
