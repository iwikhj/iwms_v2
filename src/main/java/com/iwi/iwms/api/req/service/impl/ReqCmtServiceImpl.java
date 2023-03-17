package com.iwi.iwms.api.req.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqCmt;
import com.iwi.iwms.api.req.domain.ReqCmtInfo;
import com.iwi.iwms.api.req.mapper.ReqCmtMapper;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqCmtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqCmtServiceImpl implements ReqCmtService {

	private static final String UPLOAD_PATH_PREFIX = "request/";

	private final ReqMapper reqMapper;

	private final ReqCmtMapper reqCmtMapper;
	
	private final FileService fileService;

	@Override
	public ReqCmtInfo getReqCmtBySeq(long reqCmtSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqCmtSeq", reqCmtSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqCmtMapper.getReqCmtBySeq(map))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 코멘트를 찾을 수 없습니다."));
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqCmt(ReqCmt reqCmt) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", reqCmt.getReqSeq());
		map.put("loginUserSeq", reqCmt.getLoginUserSeq());
		
		Optional.ofNullable(reqMapper.getReqBySeq(map))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
	
		reqCmtMapper.insertReqCmt(reqCmt);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(reqCmt.getFiles())) {
			UploadFile uploadFile = reqCmt.getFileInfo();
			uploadFile.setFileRefSeq(reqCmt.getReqCmtSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqCmt.getReqSeq());
			fileService.insertAttachFiles(reqCmt.getFiles(), uploadFile);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqCmt(ReqCmt reqCmt) {
		this.getReqCmtBySeq(reqCmt.getReqCmtSeq(), reqCmt.getLoginUserSeq());
		
		int result = reqCmtMapper.updateReqCmt(reqCmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqCmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, reqCmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(reqCmt.getFiles())) {
			UploadFile uploadFile = reqCmt.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqCmt.getReqSeq());
			fileService.insertAttachFiles(reqCmt.getFiles(), uploadFile);
		}
		return result;	
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqCmt(ReqCmt reqCmt) {
		this.getReqCmtBySeq(reqCmt.getReqCmtSeq(), reqCmt.getLoginUserSeq());
	
		int result = reqCmtMapper.deleteReqCmt(reqCmt);
		
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqCmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, null);
		}
		
		return result;
	}

}
