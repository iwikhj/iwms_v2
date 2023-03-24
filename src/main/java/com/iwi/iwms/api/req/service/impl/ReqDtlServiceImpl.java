package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Path;
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
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlHis;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.enums.ReqDtlStatCode;
import com.iwi.iwms.api.req.enums.ReqStatCode;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.api.user.domain.UserSiteInfo;
import com.iwi.iwms.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlServiceImpl implements ReqDtlService {
	
	private static final String UPLOAD_PATH_PREFIX = "request/";

	private final ReqMapper reqMapper;
	
	private final ReqDtlMapper reqDtlMapper;

	private final FileService fileService;
	
	private final UserService userService;
	
	@Override
	public ReqDtlInfo getReqDtlByReqAndDtlSeq(Map<String, Object> map) {
		Optional.ofNullable(reqMapper.getReqBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항을 찾을 수 없습니다."));	
		
		return reqDtlMapper.getReqDtlByReqAndDtlSeq(map);
	}
	
	@Override
	public ReqDtlInfo getReqDtlBySeq(long reqDtlSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqDtlSeq", reqDtlSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(reqDtlMapper.getReqDtlBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항 상세를 찾을 수 없습니다."));				
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtl(ReqDtl reqDtl) {
		Map<String, Object> map = new HashMap<>();
		map.put("reqSeq", reqDtl.getReqSeq());
		map.put("loginUserSeq", reqDtl.getLoginUserSeq());
		
		ReqInfo reqInfo = Optional.ofNullable(reqMapper.getReqBySeq(map))
			.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "요청사항을 찾을 수 없습니다."));				
		
		ReqStatCode status = ReqStatCode.findByCode(reqInfo.getReqStatCd());
		
		if(status != ReqStatCode.AGREE) {
        	throw new CommonException(ErrorCode.STATUS_ERROR, "현재 " + status.getMessage() + "중 입니다. " + ReqStatCode.AGREE.getMessage() + " 상태에서만 담당자 배정을 진행할 수 있습니다.");
		}
		
		if(CollectionUtils.isEmpty(reqDtl.getReqDtlUserSeqs())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "담당자는 필수 입력 사항입니다.");
		} else if(CollectionUtils.isEmpty(reqDtl.getTgtMms())) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "작업 공수는 필수 입력 사항입니다.");
		} else if(reqDtl.getReqDtlUserSeqs().size() < reqDtl.getTgtMms().size()) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "담당자보다 작업 공수가 더 많이 입력되었습니다.");
		}  else if(reqDtl.getReqDtlUserSeqs().size() != reqDtl.getTgtMms().size()) {
        	throw new CommonException(ErrorCode.PARAMETER_MALFORMED, "작업 공수는 필수 입력 사항입니다.");
		} 
		
		ReqDtlStatCode dtlStatus = ReqDtlStatCode.RECEIPT;	//RECEIPT
		
		for(int i = 0; i < reqDtl.getReqDtlUserSeqs().size(); i++) {
			long reqDtlUserSeq = reqDtl.getReqDtlUserSeqs().get(i);
			int tgtMm = reqDtl.getTgtMms().get(i);
			
			List<UserSiteInfo> siteList = userService.listSiteByUserSeq(reqDtlUserSeq);
			if(!siteList.stream().anyMatch(v -> v.getSiteSeq() == reqInfo.getSiteSeq())) {
	        	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "프로젝트 사이트에 등록된 사용자가 아닙니다.(" + i + ")");
			}
					
			reqDtl.setReqDtlUserSeq(reqDtlUserSeq);
			reqDtl.setTgtMm(tgtMm);
			reqDtl.setReqDtlStatCd(dtlStatus.getCode());
			reqDtlMapper.insertReqDtl(reqDtl);
			
			ReqDtlHis reqDtlHis = ReqDtlHis.builder()
					.reqDtlSeq(reqDtl.getReqDtlSeq())
					.reqDtlStatCd(dtlStatus.getCode())
					.reqDtlStatCmt(dtlStatus.getMessage())
					.loginUserSeq(reqDtl.getLoginUserSeq())
					.build();
			
			reqDtlMapper.insertReqDtlHis(reqDtlHis);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtl(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		int result = reqDtlMapper.updateReqDtl(reqDtl);
		
		return result;		
		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtl(ReqDtl reqDtl) {
		ReqDtlInfo reqDtlInfo = this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		
		int result = reqDtlMapper.deleteReqDtl(reqDtl);
		
		//  요청사항 상세 디렉토리 삭제
		if(!CollectionUtils.isEmpty(reqDtlInfo.getComments())) {
			Path path = Paths.get(UPLOAD_PATH_PREFIX)
					.resolve(String.valueOf(reqDtlInfo.getReqSeq()))
					.resolve(String.valueOf(reqDtlInfo.getReqDtlSeq()));
			fileService.deletePath(path);
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlStatByInProgress(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
		
		ReqDtlStatCode status = ReqDtlStatCode.IN_PROGRESS;	//IN_PROGRESS
		
		reqDtl.setReqDtlStatCd(status.getCode());
		reqDtlMapper.updateReqDtlStatByInProgress(reqDtl);
		
		ReqDtlHis reqDtlHis = ReqDtlHis.builder()
				.reqDtlSeq(reqDtl.getReqDtlSeq())
				.reqDtlStatCd(status.getCode())
				.reqDtlStatCmt(status.getMessage())
				.loginUserSeq(reqDtl.getLoginUserSeq())
				.build();
		
		reqDtlMapper.insertReqDtlHis(reqDtlHis);
		
		return 1;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlStatByProcessed(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());

		ReqDtlStatCode status = ReqDtlStatCode.PROCESSED;	//PROCESSED
		
		reqDtl.setReqDtlStatCd(status.getCode());
		reqDtlMapper.updateReqDtlStatByProcessed(reqDtl);
		
		ReqDtlHis reqDtlHis = ReqDtlHis.builder()
				.reqDtlSeq(reqDtl.getReqDtlSeq())
				.reqDtlStatCd(status.getCode())
				.reqDtlStatCmt(status.getMessage())
				.loginUserSeq(reqDtl.getLoginUserSeq())
				.build();
		
		reqDtlMapper.insertReqDtlHis(reqDtlHis);
		
		return 1;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlStatByInspectionCompleted(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());

		ReqDtlStatCode status = ReqDtlStatCode.INSPECTION_COMPLETED;	//INSPECTION_COMPLETED
		
		reqDtl.setReqDtlStatCd(status.getCode());
		reqDtlMapper.updateReqDtlStatByInspectionCompleted(reqDtl);
		
		ReqDtlHis reqDtlHis = ReqDtlHis.builder()
				.reqDtlSeq(reqDtl.getReqDtlSeq())
				.reqDtlStatCd(status.getCode())
				.reqDtlStatCmt(status.getMessage())
				.loginUserSeq(reqDtl.getLoginUserSeq())
				.build();
		
		reqDtlMapper.insertReqDtlHis(reqDtlHis);
		
		return 1;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlStatByCancel(ReqDtl reqDtl) {
		this.getReqDtlBySeq(reqDtl.getReqDtlSeq(), reqDtl.getLoginUserSeq());
	
		ReqDtlStatCode status = ReqDtlStatCode.CANCEL;	//CANCEL
		
		reqDtl.setReqDtlStatCd(status.getCode());
		reqDtlMapper.updateReqDtlStatByCancel(reqDtl);
		
		ReqDtlHis reqDtlHis = ReqDtlHis.builder()
				.reqDtlSeq(reqDtl.getReqDtlSeq())
				.reqDtlStatCd(status.getCode())
				.reqDtlStatCmt(status.getMessage())
				.loginUserSeq(reqDtl.getLoginUserSeq())
				.build();
		
		reqDtlMapper.insertReqDtlHis(reqDtlHis);
		
		return 1;
	}

}
