package com.iwi.iwms.api.comp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;

@Mapper
public interface SiteMapper {

	List<SiteInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	SiteInfo findBySeq(Site site);
	
	void save(Site site);
	
	int update(Site site);
	
	int delete(Site site);
}
