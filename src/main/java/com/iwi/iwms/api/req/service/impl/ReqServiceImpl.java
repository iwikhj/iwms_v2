package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Paths;
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
import com.iwi.iwms.api.comp.domain.DeptInfo;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.His;
import com.iwi.iwms.api.req.domain.Req;
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
	
	private final ReqMapper reqMapper;
	
	private final FileService fileService;
    
	private String getUploadPath(long reqSeq) {
		return "request/" + reqSeq;
	}
	
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
			
			
			His his = His.builder()
					.reqSeq(req.getReqSeq())
					.statCd(status.getCode())
					.statCmt(status.getMessage())
					.loginUserSeq(req.getLoginUserSeq())
					.build();
			
			reqMapper.insertReqHis(his);
			
			// 첨부파일 저장
			if(!CollectionUtils.isEmpty(req.getFiles())) {
				UploadFile uploadFile = req.getFileInfo();
				uploadFile.setFileRefSeq(req.getReqSeq());
				uploadFile.setFileRealPath(this.getUploadPath(req.getReqSeq()));
				fileService.insertFiles(req.getFiles(), uploadFile);
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
			fileService.deleteFiles(attachedFiles, req.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(req.getFiles())) {
			UploadFile uploadFile = req.getFileInfo();
			uploadFile.setFileRealPath(this.getUploadPath(req.getReqSeq()));
			fileService.insertFiles(req.getFiles(), uploadFile);
		}
		return result;		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReq(Req req) {
		this.getReqBySeq(req.getReqSeq(), req.getLoginUserSeq());
		
		int result = reqMapper.deleteReq(req);
		
		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(req.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteFiles(attachedFiles, null);
		}
		
		// 폴더 삭제
		fileService.deleteFolder(Paths.get(this.getUploadPath(req.getReqSeq())));
		
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void updateReqStat(His his) {
		ReqInfo reqInfo = this.getReqBySeq(his.getReqSeq(), his.getLoginUserSeq());
		
		ReqStatCode oldStat = ReqStatCode.findByCode(reqInfo.getStatCd());
		ReqStatCode newStat = ReqStatCode.findByCode(his.getStatCd());
		log.info("[요청사항 상태 변경] <{} -> {}>", oldStat.getMessage(), newStat.getMessage());
		
		if(oldStat == newStat) {
        	throw new CommonException(ErrorCode.DUPLICATE_ERROR, "이미 " + newStat.getMessage() + " 상태입니다.");
		}
		
		if(newStat == ReqStatCode.REQUEST && (oldStat != ReqStatCode.NEGO_CHANGE || oldStat != ReqStatCode.NEGO_ADD)) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + "중 입니다. 협의요청 상태에서만 재요청을 진행할 수 있습니다.");
		}
		if(newStat == ReqStatCode.AGREE && oldStat != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 합의를 진행할 수 있습니다.");
		}
		if((newStat == ReqStatCode.NEGO_CHANGE || newStat == ReqStatCode.NEGO_ADD) && oldStat != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 협의요청을 진행할 수 있습니다.");
		}
		if(newStat == ReqStatCode.REJECT && oldStat != ReqStatCode.REQUEST) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + "중 입니다. " + ReqStatCode.REQUEST.getMessage() + " 상태에서만 반려를 진행할 수 있습니다.");
		}
		if(!StringUtils.hasText(his.getStatCmt())) {
			his.setStatCmt(newStat.getMessage());
		}
		
		reqMapper.insertReqHis(his);
	}

	@Override
	public List<DeptInfo> listDeptForTask(long projSeq) {
		return reqMapper.listDeptForTask(projSeq);
	}

}
