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
import com.iwi.iwms.api.file.enums.UploadType;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.CmtInfo;
import com.iwi.iwms.api.req.mapper.ReqDtlCmtMapper;
import com.iwi.iwms.api.req.service.ReqDtlCmtService;
import com.iwi.iwms.api.req.service.ReqDtlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlCmtServiceImpl implements ReqDtlCmtService {
	
	private final ReqDtlCmtMapper reqDtlCmtMapper;
	
	private final ReqDtlService reqDtlService;

	private final FileService fileService;
	
	@Override
	public CmtInfo getReqDtlCmtBySeq(long cmtSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("cmtSeq", cmtSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqDtlCmtMapper.getReqDtlCmtBySeq(map))
					.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "작업 코멘트를 찾을 수 없습니다."));				
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public CmtInfo insertReqDtlCmt(Cmt cmt) {
		reqDtlService.getReqDtlBySeq(cmt.getReqSeq(), cmt.getReqDtlSeq(), cmt.getLoginUserSeq());

		reqDtlCmtMapper.insertReqDtlCmt(cmt);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles()) && !cmt.getFiles().stream().findFirst().get().isEmpty()) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRefSeq(cmt.getCmtSeq());
			uploadFile.setFileRealPath(UploadType.REQUEST_TASK_CMT.getPath(cmt.getReqSeq(), cmt.getReqDtlSeq(), cmt.getCmtSeq()));
			fileService.insertFiles(cmt.getFiles(), uploadFile);
		}
		
		return this.getReqDtlCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public CmtInfo updateReqDtlCmt(Cmt cmt) {
		this.getReqDtlCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
		
		reqDtlCmtMapper.updateReqDtlCmt(cmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(cmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteFiles(attachedFiles, cmt.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(cmt.getFiles()) && !cmt.getFiles().stream().findFirst().get().isEmpty()) {
			UploadFile uploadFile = cmt.getFileInfo();
			uploadFile.setFileRealPath(UploadType.REQUEST_TASK_CMT.getPath(cmt.getReqSeq(), cmt.getReqDtlSeq(), cmt.getCmtSeq()));
			fileService.insertFiles(cmt.getFiles(), uploadFile);
		}
		
		return this.getReqDtlCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());
	
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtlCmt(Cmt cmt) {
		this.getReqDtlCmtBySeq(cmt.getCmtSeq(), cmt.getLoginUserSeq());

		int result = reqDtlCmtMapper.deleteReqDtlCmt(cmt);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(cmt.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteFiles(attachedFiles, null);
		}
		
		// 폴더 삭제
		fileService.deleteFolder(Paths.get(UploadType.REQUEST_TASK_CMT.getPath(cmt.getReqSeq(), cmt.getReqDtlSeq(), cmt.getCmtSeq())));
		
		return result;
	}
}
