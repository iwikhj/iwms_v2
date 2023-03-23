package com.iwi.iwms.api.comp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.comp.domain.Proj;
import com.iwi.iwms.api.comp.domain.ProjInfo;
import com.iwi.iwms.api.comp.domain.ProjUser;
import com.iwi.iwms.api.comp.domain.ProjUserInfo;
import com.iwi.iwms.api.comp.domain.Site;
import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.mapper.ProjMapper;
import com.iwi.iwms.api.comp.service.ProjService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjServiceImpl implements ProjService {

	private final ProjMapper projMapper;
	
	@Override
	public List<ProjInfo> listProj(Map<String, Object> map) {
		return projMapper.listProj(map);
	}

	@Override
	public int countProj(Map<String, Object> map) {
		return projMapper.countProj(map);
	}

	@Override
	public ProjInfo getProjBySeq(long projSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("projSeq", projSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(projMapper.getProjBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "프로젝트를 찾을 수 없습니다."));
		
	}

	@Override
	public void insertProj(Proj proj) {
		projMapper.insertProj(proj);
	}

	@Override
	public int updateProj(Proj proj) {
		this.getProjBySeq(proj.getProjSeq(), proj.getLoginUserSeq());
		return projMapper.updateProj(proj);
	}

	@Override
	public int deleteProj(Proj proj) {
		this.getProjBySeq(proj.getProjSeq(), proj.getLoginUserSeq());
		return projMapper.deleteProj(proj);
	}

	@Override
	public List<ProjUserInfo> listCustProjUser(long projSeq) {
		return projMapper.listCustProjUser(projSeq);
	}

	@Override
	public List<ProjUserInfo> listPerfProjUser(long projSeq) {
		return projMapper.listPerfProjUser(projSeq);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateProjUser(ProjUser projUser) {
		this.getProjBySeq(projUser.getProjSeq(), projUser.getLoginUserSeq());
		projMapper.deleteProjUser(projUser);
		return projMapper.updateProjUser(projUser);
	}
	
	@Override
	public List<SiteInfo> listSite(Map<String, Object> map) {
		return projMapper.listSite(map);
	}

	@Override
	public int countSite(Map<String, Object> map) {
		return projMapper.countSite(map);
	}

	@Override
	public SiteInfo getSiteBySeq(long siteSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("siteSeq", siteSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(projMapper.getSiteBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "사이트를 찾을 수 없습니다."));
	}

	@Override
	public void insertSite(Site site) {
		this.getProjBySeq(site.getProjSeq(), site.getLoginUserSeq());
		projMapper.insertSite(site);
	}

	@Override
	public int updateSite(Site site) {
		this.getSiteBySeq(site.getSiteSeq(), site.getLoginUserSeq());
		return projMapper.updateSite(site);
	}

	@Override
	public int deleteSite(Site site) {
		this.getSiteBySeq(site.getSiteSeq(), site.getLoginUserSeq());		
		return projMapper.deleteSite(site);
	}
}
