package com.iwi.iwms.api.req.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqCmt;
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
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqCmt(ReqCmt reqCmt) {
		Optional.ofNullable(reqMapper.getReqBySeq(reqCmt.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
	
		reqCmtMapper.insertReqCmt(reqCmt);
		
		// 첨부파일 저장
		if(reqCmt.getFiles() != null && !reqCmt.getFiles().isEmpty()) {
			UploadFile uploadFile = reqCmt.getFileInfo();
			uploadFile.setFileRefSeq(reqCmt.getReqCmtSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqCmt.getReqSeq());
			fileService.insertAttachFiles(reqCmt.getFiles(), uploadFile);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqCmt(ReqCmt reqCmt) {
		Optional.ofNullable(reqCmtMapper.getReqCmtBySeq(reqCmt.getReqCmtSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 코멘트를 찾을 수 없습니다."));
		
		int result = reqCmtMapper.updateReqCmt(reqCmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqCmt.getFileInfo());
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachFiles(attachedFiles, reqCmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(reqCmt.getFiles() != null && !reqCmt.getFiles().isEmpty()) {
			UploadFile uploadFile = reqCmt.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqCmt.getReqSeq());
			fileService.insertAttachFiles(reqCmt.getFiles(), uploadFile);
		}
		return result;	
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqCmt(ReqCmt reqCmt) {
		Optional.ofNullable(reqCmtMapper.getReqCmtBySeq(reqCmt.getReqCmtSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 코멘트를 찾을 수 없습니다."));
	
		int result = reqCmtMapper.deleteReqCmt(reqCmt);
		
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqCmt.getFileInfo());
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachFiles(attachedFiles, null);
		}
		
		return result;
	}

}
