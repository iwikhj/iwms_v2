package com.iwi.iwms.api.req.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.mapper.ReqDtlCmtMapper;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.service.ReqDtlCmtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlCmtServiceImpl implements ReqDtlCmtService {
	
	private static final String UPLOAD_PATH_PREFIX = "request/";
	
	private final ReqDtlMapper reqDtlMapper;
	
	private final ReqDtlCmtMapper reqDtlCmtMapper;

	private final FileService fileService;

	@Override
	public ReqDtlCmtInfo getReqDtlCmtBySeq(long reqDtlCmtSeq) {
		return Optional.ofNullable(reqDtlCmtMapper.getReqDtlCmtBySeq(reqDtlCmtSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 상세 코멘트를 찾을 수 없습니다."));
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		ReqDtlInfo reqDtlInfo = Optional.ofNullable(reqDtlMapper.getReqDtlBySeq(reqDtlCmt.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 상세를 찾을 수 없습니다."));
		
		reqDtlCmtMapper.insertReqDtlCmt(reqDtlCmt);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(reqDtlCmt.getFiles())) {
			UploadFile uploadFile = reqDtlCmt.getFileInfo();
			uploadFile.setFileRefSeq(reqDtlCmt.getReqDtlCmtSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqDtlInfo.getReqSeq() + "/" + reqDtlCmt.getReqDtlSeq() + "/" + reqDtlCmt.getReqDtlCmtSeq());
			fileService.insertAttachFiles(reqDtlCmt.getFiles(), uploadFile);
		}
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		ReqDtlCmtInfo reqDtlCmtInfo = this.getReqDtlCmtBySeq(reqDtlCmt.getReqDtlCmtSeq());
		
		int result = reqDtlCmtMapper.updateReqDtlCmt(reqDtlCmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqDtlCmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, reqDtlCmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(reqDtlCmt.getFiles())) {
			UploadFile uploadFile = reqDtlCmt.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + reqDtlCmtInfo.getReqSeq() + "/" + reqDtlCmt.getReqDtlSeq() + "/" + reqDtlCmt.getReqDtlCmtSeq());
			fileService.insertAttachFiles(reqDtlCmt.getFiles(), uploadFile);
		}
		return result;	
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		this.getReqDtlCmtBySeq(reqDtlCmt.getReqDtlCmtSeq());

		int result = reqDtlCmtMapper.deleteReqDtlCmt(reqDtlCmt);
		
		// 첨부파일 삭제(디렉토리까지)
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(reqDtlCmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachAll(attachedFiles);
		}
		return result;
	}

}
