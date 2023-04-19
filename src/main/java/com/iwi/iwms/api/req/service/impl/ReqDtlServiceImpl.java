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
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.enums.UploadType;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.login.domain.LoginUserInfo;
import com.iwi.iwms.api.req.domain.Cmt;
import com.iwi.iwms.api.req.domain.His;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.enums.ReqDtlStatCode;
import com.iwi.iwms.api.req.enums.ReqStatCode;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlServiceImpl implements ReqDtlService {
	
	private final ReqDtlMapper reqDtlMapper;
	
	private final ReqService reqService;

	private final FileService fileService;
	
	private final UserService userService;
	
	@Override
	public ReqDtlInfo getReqDtlByReqSeq(long reqSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", reqSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		if(reqDtlMapper.countReqDtlByReqSeq(map) > 0) {
			throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "이미 담당자가 배정된 요청사항입니다. 작업을 지정해서 요청해주세요.");
		}
		
		return Optional.ofNullable(reqDtlMapper.getReqDtlByReqSeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항을 찾을 수 없습니다."));		
	}
	
	@Override
	public ReqDtlInfo getReqDtlBySeq(long reqSeq, long reqDtlSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", reqSeq);
		map.put("reqDtlSeq", reqDtlSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqDtlMapper.getReqDtlBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "작업을 찾을 수 없습니다."));				
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtl(ReqDtl reqDtl) {
		ReqInfo reqInfo = reqService.getReqBySeq(reqDtl.getReqSeq(), reqDtl.getLoginUserSeq());
		
		ReqStatCode reqStatus = ReqStatCode.findByCode(reqInfo.getStatCd());
		
		if(reqStatus != ReqStatCode.AGREE) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + reqStatus.getMessage() + "중 입니다. " + ReqStatCode.AGREE.getMessage() + " 상태에서만 담당자 배정을 진행할 수 있습니다.");
		}
		
		if(CollectionUtils.isEmpty(reqDtl.getReqDtlUserSeqs())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "담당자는 필수 입력 사항입니다.");
		} else if(CollectionUtils.isEmpty(reqDtl.getTgtMms())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "작업 공수는 필수 입력 사항입니다.");
		} else if(CollectionUtils.isEmpty(reqDtl.getTasks())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "작업 내용은 필수 입력 사항입니다.");
		} 
		
		boolean flag = false;
		
		if(reqDtl.getReqDtlUserSeqs().size() == reqDtl.getTgtMms().size()) {
			if(reqDtl.getReqDtlUserSeqs().size() == reqDtl.getTasks().size()) {
				flag = true;
			}
		}
		
		if(!flag) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "담당자, 작업 공수, 작업 내용이 잘못 입력 되었습니다.");
		}
		
		ReqDtlStatCode status = ReqDtlStatCode.RECEIPT;	//RECEIPT
		
		for(int i = 0; i < reqDtl.getReqDtlUserSeqs().size(); i++) {
			long reqDtlUserSeq = reqDtl.getReqDtlUserSeqs().get(i);
			int tgtMm = reqDtl.getTgtMms().get(i);
			String task = reqDtl.getTasks().get(i);
			
			List<UserSiteInfo> siteList = userService.listSiteByUserSeq(reqDtlUserSeq);
			if(!siteList.stream().anyMatch(v -> v.getSiteSeq() == reqInfo.getSiteSeq())) {
	        	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "프로젝트 사이트에 등록된 사용자가 아닙니다.(" + i + ")");
			}
					
			reqDtl.setReqDtlUserSeq(reqDtlUserSeq);
			reqDtl.setTgtMm(tgtMm);
			reqDtl.setTask(task);
			reqDtl.setStatCd(status.getCode());
			
			reqDtlMapper.insertReqDtl(reqDtl);
			
			His his = His.builder()
					.reqDtlSeq(reqDtl.getReqDtlSeq())
					.statCd(status.getCode())
					.statCmt(status.getMessage())
					.loginUserSeq(reqDtl.getLoginUserSeq())
					.build();
			
			reqDtlMapper.insertReqDtlHis(his);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtl(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqSeq(), reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		int result = reqDtlMapper.updateReqDtl(reqDtl);
		
		return result;		
		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtl(ReqDtl reqDtl) {
		ReqDtlInfo reqDtlInfo = this.getReqDtlBySeq(reqDtl.getReqSeq(), reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		
		int result = reqDtlMapper.deleteReqDtl(reqDtl);
		
		//  요청사항 상세 코멘트 파일 삭제
		if(!CollectionUtils.isEmpty(reqDtlInfo.getComments())) {
			reqDtlInfo.getComments().stream()
				.map(v -> Cmt.builder()
							.reqSeq(v.getReqSeq())
							.reqDtlSeq(v.getReqDtlSeq())
							.cmtSeq(v.getCmtSeq())
							.build()
							.of(LoginUserInfo.builder()
									.userSeq(reqDtl.getLoginUserSeq())
									.build())
				)
				.forEach(v -> {
					List<UploadFileInfo> attachedFiles = fileService.listFileByRef(v.getFileInfo());
					if(!CollectionUtils.isEmpty(attachedFiles)) {
						fileService.deleteFiles(attachedFiles, null);
					}
				});
		}
		
		// 폴더 삭제
		fileService.deleteFolder(Paths.get(UploadType.REQUEST_TASK.getPath(reqDtlInfo.getReqSeq(), reqDtlInfo.getReqDtlSeq())));
		
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void updateReqDtlStat(ReqDtl reqDtl) {
		ReqDtlInfo reqDtlInfo = this.getReqDtlBySeq(reqDtl.getReqSeq(), reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		
		ReqDtlStatCode oldStat = ReqDtlStatCode.findByCode(reqDtlInfo.getStatCd());
		ReqDtlStatCode newStat = ReqDtlStatCode.findByCode(reqDtl.getStatCd());
		log.info("[작업 상태 변경] <{} -> {}>", oldStat.getMessage(), newStat.getMessage());
		
		if(oldStat == newStat) {
        	throw new CommonException(ErrorCode.DUPLICATE_ERROR, "이미 " + newStat.getMessage() + " 상태입니다.");
		}
		if(newStat == ReqDtlStatCode.IN_PROGRESS && oldStat != ReqDtlStatCode.RECEIPT) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + " 상태입니다. " + ReqDtlStatCode.RECEIPT.getMessage() + " 상태에서만 담당자 확인을 진행할 수 있습니다.");
		}
		if(newStat == ReqDtlStatCode.PROCESSED && oldStat != ReqDtlStatCode.IN_PROGRESS) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + " 상태입니다. " + ReqDtlStatCode.IN_PROGRESS.getMessage() + " 상태에서만 처리 완료를 진행할 수 있습니다.");
		}
		if(newStat == ReqDtlStatCode.INSPECTION_COMPLETED && oldStat != ReqDtlStatCode.PROCESSED) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + oldStat.getMessage() + " 상태입니다. " + ReqDtlStatCode.PROCESSED.getMessage() + " 상태에서만 검수 완료를 진행할 수 있습니다.");
		}
		
		int result = 0; 
		if(newStat == ReqDtlStatCode.IN_PROGRESS) {
			result = reqDtlMapper.updateReqDtlStatInProgress(reqDtl);
		} else if(newStat == ReqDtlStatCode.PROCESSED) {
			result = reqDtlMapper.updateReqDtlStatProcessed(reqDtl);
		} else if(newStat == ReqDtlStatCode.INSPECTION_COMPLETED) {
			result = reqDtlMapper.updateReqDtlStatInspectionCompleted(reqDtl);
		} else if(newStat == ReqDtlStatCode.CANCEL) {
			result = reqDtlMapper.updateReqDtlStatCancel(reqDtl);
		}
		
		if(result == 0) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "작업 상태 변경에 실패했습니다.");
		}
		
		His his = His.builder()
				.reqDtlSeq(reqDtl.getReqDtlSeq())
				.statCd(newStat.getCode())
				.statCmt(newStat.getMessage())
				.loginUserSeq(reqDtl.getLoginUserSeq())
				.build();
		
		reqDtlMapper.insertReqDtlHis(his);
	}

}
