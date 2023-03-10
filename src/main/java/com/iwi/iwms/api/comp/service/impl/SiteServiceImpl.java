package com.iwi.iwms.api.comp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.mapper.ProjMapper;
import com.iwi.iwms.api.comp.mapper.SiteMapper;
import com.iwi.iwms.api.comp.service.SiteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

	private final ProjMapper projMapper;
	
	private final SiteMapper siteMapper;
	
	@Override
	public List<SiteInfo> listSite(Map<String, Object> map) {
		return siteMapper.listSite(map);
	}

	@Override
	public int countSite(Map<String, Object> map) {
		return siteMapper.countSite(map);
	}

	@Override
	public SiteInfo getSiteBySeq(long siteSeq) {
		return Optional.ofNullable(siteMapper.getSiteBySeq(siteSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 찾을 수 없습니다."));
	}

	@Override
	public void insertSite(Site site) {
		Optional.ofNullable(projMapper.getProjBySeq(site.getProjSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));	
		siteMapper.insertSite(site);
	}

	@Override
	public int updateSite(Site site) {
		this.getSiteBySeq(site.getSiteSeq());
		return siteMapper.updateSite(site);
	}

	@Override
	public int deleteSite(Site site) {
		this.getSiteBySeq(site.getSiteSeq());		
		return siteMapper.deleteSite(site);
	}

}
