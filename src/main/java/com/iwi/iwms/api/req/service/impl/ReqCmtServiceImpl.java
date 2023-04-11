package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Paths;
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
import com.iwi.iwms.api.req.service.ReqCmtService;
import com.iwi.iwms.api.req.service.ReqService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqCmtServiceImpl implements ReqCmtService {

	private final ReqCmtMapper reqCmtMapper;
	
	private final ReqService reqService;
	
	private final FileService fileService;

	private String getUploadPath(long reqSeq, long cmtSeq) {
		return "request/" + reqSeq + "/comment/" + cmtSeq;
	}
	
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
		reqService.getReqBySeq(cmt.getReqSeq(), cmt.getLoginUserSeq());
		
		reqCmtMapper.insertReqCmt(cmt);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles())) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRefSeq(cmt.getCmtSeq());
			uploadFile.setFileRealPath(this.getUploadPath(cmt.getReqSeq(), cmt.getCmtSeq()));
			fileService.insertFiles(cmt.getFiles(), uploadFile);
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
			fileService.deleteFiles(attachedFiles, cmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles())) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRealPath(this.getUploadPath(cmt.getReqSeq(), cmt.getCmtSeq()));
			fileService.insertFiles(cmt.getFiles(), uploadFile);
		}
		
		return this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqCmt(Cmt cmt) {
		this.getReqCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	
		int result = reqCmtMapper.deleteReqCmt(cmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(cmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteFiles(attachedFiles, null);
		}
		
		// 폴더 삭제
		fileService.deleteFolder(Paths.get(this.getUploadPath(cmt.getReqSeq(), cmt.getCmtSeq())));
		
		return result;
	}
}
