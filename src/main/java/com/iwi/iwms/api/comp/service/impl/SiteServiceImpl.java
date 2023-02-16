package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.mapper.SiteMapper;
import com.iwi.iwms.api.comp.service.SiteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

	private final SiteMapper siteMapper;
	
	@Override
	public List<SiteInfo> listSite(Map<String, Object> map) {
		return siteMapper.findAll(map);
	}

	@Override
	public int countSite(Map<String, Object> map) {
		return siteMapper.count(map);
	}

	@Override
	public SiteInfo getSiteBySeq(Site site) {
		return Optional.ofNullable(siteMapper.findBySeq(site))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 찾을 수 없습니다."));
	}

	@Override
	public void insertSite(Site Site) {
		siteMapper.save(Site);
	}

	@Override
	public int updateSite(Site site) {
		Optional.ofNullable(siteMapper.findBySeq(site))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 찾을 수 없습니다."));
		
		return siteMapper.update(site);
	}

	@Override
	public int deleteSite(Site site) {
		Optional.ofNullable(siteMapper.findBySeq(site))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 찾을 수 없습니다."));
		
		return siteMapper.delete(site);
	}

}
