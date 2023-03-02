package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqDtlService;

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
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtl(ReqDtl reqDtl) {
		ReqInfo reqInfo = Optional.ofNullable(reqMapper.findBySeq(reqDtl.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항을 찾을 수 없습니다."));
		
		reqDtl.setReqNo(reqInfo.getReqNo());
		reqDtl.setReqGbCd(reqInfo.getReqGbCd());
		reqDtlMapper.save(reqDtl);
		
		reqDtlMapper.saveHistory(reqDtl);
	}

	@Override
	public int updateReqDtl(ReqDtl reqDtl) {
		Optional.ofNullable(reqDtlMapper.findBySeq(reqDtl.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항 상세를 찾을 수 없습니다."));
		
		int result = reqDtlMapper.update(reqDtl);
		
		return result;		
		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtl(ReqDtl reqDtl) {
		ReqDtlInfo reqDtlInfo = Optional.ofNullable(reqDtlMapper.findBySeq(reqDtl.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항 상세를 찾을 수 없습니다."));
		
		int result = reqDtlMapper.delete(reqDtl);
		
		//  요청사항 상세 디렉토리 삭제
		List<ReqDtlCmtInfo> comments = reqDtlInfo.getComments();
		if(comments != null && comments.size() > 0) {
			/*
			// 코멘트 첨부파일 삭제(디렉토리까지)
			comments.stream()
				.forEach(v -> {
					List<UploadFileInfo> attachedFiles = v.getAttachedFiles();
					if(attachedFiles != null && attachedFiles.size() > 0) {
						fileService.deleteAttachAll(attachedFiles);
					}
				});
			*/
			
			Path path = Paths.get(UPLOAD_PATH_PREFIX)
					.resolve(String.valueOf(reqDtlInfo.getReqSeq()))
					.resolve(String.valueOf(reqDtlInfo.getReqDtlSeq()));
			fileService.deletePath(path);
		}
		return result;
	}

}
