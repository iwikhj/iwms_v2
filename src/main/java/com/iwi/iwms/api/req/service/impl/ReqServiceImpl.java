package com.iwi.iwms.api.req.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqHis;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.enums.ReqStatCode;
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
	public ReqInfo getReqBySeq(long reqSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", reqSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqMapper.getReqBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항을 찾을 수 없습니다."));				

	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReq(Req req) {
		if(CollectionUtils.isEmpty(req.getSitesSeq())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "사이트를 선택해주세요.");
		}
		
		ReqStatCode status = ReqStatCode.REQUEST;	//REQUEST
		
		for(long siteSeq : req.getSitesSeq()) {
			req.setSiteSeq(siteSeq);
			reqMapper.insertReq(req);
			
			ReqHis reqHis = ReqHis.builder()
					.reqSeq(req.getReqSeq())
					.reqStatCd(status.getCode())
					.reqStatCmt(status.getMessage())
					.loginUserSeq(req.getLoginUserSeq())
					.build();
			
			reqMapper.insertReqHis(reqHis);
			
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
		this.getReqBySeq(req.getReqSeq(), req.getLoginUserSeq());
		
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
		this.getReqBySeq(req.getReqSeq(), req.getLoginUserSeq());
		
		int result = reqMapper.deleteReq(req);
		
		// 첨부파일 삭제(디렉토리까지)
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(req.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachAll(attachedFiles);
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqHis(ReqHis reqHis) {
		this.getReqBySeq(reqHis.getReqSeq(), reqHis.getLoginUserSeq());
		
		ReqStatCode oldStatus = ReqStatCode.findByCode(reqMapper.getCurrentReqStatBySeq(reqHis.getReqSeq()));
		ReqStatCode newStatus = ReqStatCode.findByCode(reqHis.getReqStatCd());
		log.info("[요청사항 상태 변경] <{} -> {}>", oldStatus.getMessage(), newStatus.getMessage());
		
		if(oldStatus == newStatus) {
        	throw new CommonException(ErrorCode.DUPLICATE_ERROR, "이미 " + newStatus.getMessage() + " 상태입니다.");
		}
		
		if(newStatus == ReqStatCode.REQUEST && oldStatus != ReqStatCode.NEGO) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStatus.getMessage() + "중 입니다. " + ReqStatCode.NEGO.getMessage() + " 상태에서만 재요청을 진행할 수 있습니다.");
		}
		
		if(newStatus == ReqStatCode.AGREE && oldStatus != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStatus.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 합의를 진행할 수 있습니다.");
		}
		
		if(newStatus == ReqStatCode.NEGO && oldStatus != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStatus.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 협의요청을 진행할 수 있습니다.");
		}
		
		if(newStatus == ReqStatCode.REJECT && oldStatus != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStatus.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 반려를 진행할 수 있습니다.");
		}
		
		if(!StringUtils.hasText(reqHis.getReqStatCmt())) {
			reqHis.setReqStatCmt(newStatus.getMessage());
		}
		
		reqMapper.insertReqHis(reqHis);
	}

}
