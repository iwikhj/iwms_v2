package com.iwi.iwms.api.req.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.comp.domain.SiteInfo;
import com.iwi.iwms.api.comp.mapper.SiteMapper;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqCmt;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.domain.ReqStat;
import com.iwi.iwms.api.req.enums.ReqCode;
import com.iwi.iwms.api.req.mapper.ReqCmtMapper;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqServiceImpl implements ReqService {
	
	private static final String UPLOAD_PATH_PREFIX = "request/";

	private final ReqMapper reqMapper;
	
	private final SiteMapper siteMapper;
	
	private final FileService fileService;
	
	private final ReqCmtMapper reqCmtMapper;
    
	@Override
	public List<ReqInfo> listReq(Map<String, Object> map) {
		return reqMapper.listReq(map);
	}

	@Override
	public int countReq(Map<String, Object> map) {
		return reqMapper.countReq(map);
	}

	@Override
	public ReqInfo getReqBySeq(long reqSeq) {
		return Optional.ofNullable(reqMapper.getReqBySeq(reqSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReq(Req req) {
		if(CollectionUtils.isEmpty(req.getSitesSeq())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 선택해주세요.");
		}
		
		AtomicInteger index = new AtomicInteger();   
		List<SiteInfo> sites = req.getSitesSeq()
			.stream()
			.map(v -> {
				int idx = index.getAndIncrement();
				return  Optional.ofNullable(siteMapper.getSiteBySeq(v))
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사이트를 찾을 수 없습니다. [" + idx + "]"));
			})
			.collect(Collectors.toList());
		
		for(SiteInfo site : sites) {
			
			req.setSiteSeq(site.getSiteSeq());
			req.setProjSeq(site.getProjSeq());
			
			reqMapper.insertReq(req);
			
			ReqCmt reqCmt = ReqCmt.builder()
					.reqSeq(req.getReqSeq())
					.reqCmt(ReqCode.REQ.getMessage())
					.regSeq(req.getRegSeq())
					.build();
			
			reqCmtMapper.insertReqCmt(reqCmt);
			
			// 첨부파일 저장
			if(!CollectionUtils.isEmpty(req.getFiles())) {
				UploadFile uploadFile = req.getFileInfo();
				uploadFile.setFileRefSeq(req.getReqSeq());
				uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + req.getReqSeq());
				fileService.insertAttachFiles(req.getFiles(), uploadFile);
			}
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReq(Req req) {
		Optional.ofNullable(reqMapper.getReqBySeq(req.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		int result = reqMapper.updateReq(req);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(req.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, req.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(req.getFiles())) {
			UploadFile uploadFile = req.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + req.getReqSeq());
			fileService.insertAttachFiles(req.getFiles(), uploadFile);
		}
		return result;		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReq(Req req) {
		Optional.ofNullable(reqMapper.getReqBySeq(req.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		int result = reqMapper.deleteReq(req);
		
		// 첨부파일 삭제(디렉토리까지)
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(req.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachAll(attachedFiles);
		}
		return result;
	}
	
	@Override
	public int updateReqStatus(ReqStat reqStat) {
		Optional.ofNullable(reqMapper.getReqBySeq(reqStat.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		ReqCmt reqCmt = ReqCmt.builder()
				.reqSeq(reqStat.getReqSeq())
				.reqCmt(ReqCode.getReqCode(reqStat.getReqStatCd()).getMessage())
				.regSeq(reqStat.getUpdtSeq())
				.build();
		
		reqCmtMapper.insertReqCmt(reqCmt);
		
		return reqMapper.updateReqStatus(reqStat);
	}

}
