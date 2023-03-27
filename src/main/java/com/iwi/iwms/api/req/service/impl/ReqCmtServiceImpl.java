package com.iwi.iwms.api.req.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;
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
	public CmtInfo getReqCmtBySeq(long cmtSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("cmtSeq", cmtSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqCmtMapper.getReqCmtBySeq(map))
					.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항 코멘트를 찾을 수 없습니다."));				
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public CmtInfo insertReqCmt(Cmt cmt) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", cmt.getReqSeq());
		map.put("loginUserSeq", cmt.getLoginUserSeq());
		
		Optional.ofNullable(reqMapper.getReqBySeq(map))
			.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항을 찾을 수 없습니다."));				
	
		reqCmtMapper.insertReqCmt(cmt);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles())) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRefSeq(cmt.getCmtSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + cmt.getReqSeq());
			fileService.insertAttachFiles(cmt.getFiles(), uploadFile);
		}
		
		return this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public CmtInfo updateReqCmt(Cmt cmt) {
		this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
		
		reqCmtMapper.updateReqCmt(cmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(cmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, cmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles())) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + cmt.getReqSeq());
			fileService.insertAttachFiles(cmt.getFiles(), uploadFile);
		}
		
		return this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqCmt(Cmt cmt) {
		this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	
		int result = reqCmtMapper.deleteReqCmt(cmt);
		
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(cmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, null);
		}
		
		return result;
	}

}
