package com.iwi.iwms.api.req.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqAgree;
import com.iwi.iwms.api.req.domain.ReqCancel;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;
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
	
	private final FileService fileService;
    
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
	
		reqMapper.insertReq(req);
		
		// 첨부파일 저장
		if(req.getFiles() != null && !req.getFiles().isEmpty()) {
			UploadFile uploadFile = req.getFileInfo();
			uploadFile.setFileRefSeq(req.getReqSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + req.getReqSeq());
			fileService.insertAttachFiles(req.getFiles(), uploadFile);
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
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachFiles(attachedFiles, req.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(req.getFiles() != null && !req.getFiles().isEmpty()) {
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
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachAll(attachedFiles);
		}
		return result;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int cancelReq(ReqCancel reqCancel) {
		Optional.ofNullable(reqMapper.getReqBySeq(reqCancel.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		return reqMapper.cancelReq(reqCancel);
	}
	
	@Override
	public int agreeReq(ReqAgree reqAgree) {
		Optional.ofNullable(reqMapper.getReqBySeq(reqAgree.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		return reqMapper.agreeReq(reqAgree);
	}

}
