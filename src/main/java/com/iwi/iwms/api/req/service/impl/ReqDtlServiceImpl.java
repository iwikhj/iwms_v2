package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.mapper.FileMapper;
import com.iwi.iwms.api.req.domain.ReqDtl;
import com.iwi.iwms.api.req.domain.ReqDtlCmt;
import com.iwi.iwms.api.req.domain.ReqDtlCmtInfo;
import com.iwi.iwms.api.req.domain.ReqDtlInfo;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqDtlService;
import com.iwi.iwms.filestorage.service.FileStorageService;
import com.iwi.iwms.utils.FilePolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlServiceImpl implements ReqDtlService {
	
	private final ReqMapper reqMapper;
	
	private final ReqDtlMapper reqDtlMapper;
	
	private final FileMapper fileMapper;
	
    private final FileStorageService fileStorageService;

	@Override
	public ReqDtlInfo getReqDtlBySeq(long reqDtlSeq) {
		return Optional.ofNullable(reqDtlMapper.findBySeq(reqDtlSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtl(ReqDtl reqDtl) {
		ReqInfo reqInfo = Optional.ofNullable(reqMapper.findBySeq(reqDtl.getReqSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유지보수건을 찾을 수 없습니다."));
		
		reqDtl.setReqNo(reqInfo.getReqNo());
		reqDtl.setReqGbCd(reqInfo.getReqGbCd());
		reqDtlMapper.save(reqDtl);
		
		reqDtlMapper.saveHistory(reqDtl);
	}

	@Override
	public int updateReqDtl(ReqDtl reqDtl) {
		Optional.ofNullable(reqDtlMapper.findBySeq(reqDtl.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항을 찾을 수 없습니다."));
		
		int result = reqDtlMapper.update(reqDtl);
		
		return result;		
		
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtl(ReqDtl reqDtl) {
		Optional.ofNullable(reqDtlMapper.findBySeq(reqDtl.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항을 찾을 수 없습니다."));
		
		int result = reqDtlMapper.delete(reqDtl);
		
		return result;
	}

	@Override
	public ReqDtlCmtInfo getReqDtlCmtBySeq(long reqDtlCmtSeq) {
		return Optional.ofNullable(reqDtlMapper.findCommentBySeq(reqDtlCmtSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항 코멘트를 찾을 수 없습니다."));
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		ReqDtlInfo reqDtlInfo = Optional.ofNullable(reqDtlMapper.findBySeq(reqDtlCmt.getReqDtlSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항을 찾을 수 없습니다."));
		
		reqDtlCmt.setReqDtlNo(reqDtlInfo.getReqDtlNo());
		reqDtlMapper.saveComment(reqDtlCmt);
		log.info("reqDtlCmtSeq: {}, 첨부파일: {}", reqDtlCmt.getReqDtlCmtSeq(), reqDtlCmt.getFiles());
		
		if(reqDtlCmt.getFiles() != null && !reqDtlCmt.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = reqDtlCmt.getFileInfo();
			uploadFile.setFileRefSeq(reqDtlCmt.getReqDtlCmtSeq());
			uploadFile.setFileRealPath("request/" + reqDtlCmt.getReqSeq());
			
			reqDtlCmt.getFiles().stream()
				.forEach(v -> {
					uploadFile.setFileOrgNm(v.getOriginalFilename());
					uploadFile.setFileRealNm(FilePolicy.rename(v.getOriginalFilename()));
					fileMapper.save(uploadFile);
					
			    	log.info("fileSeq: {}", v.getOriginalFilename());
					fileStorageService.store(v, Paths.get(uploadFile.getFileRealPath()), uploadFile.getFileRealNm());
				});
			
	    	fileMapper.updateOrderNum(uploadFile);
		}

	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		Optional.ofNullable(reqDtlMapper.findCommentBySeq(reqDtlCmt.getReqDtlCmtSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항 코멘트를 찾을 수 없습니다."));
		
		int result = reqDtlMapper.updateComment(reqDtlCmt);
		
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(reqDtlCmt.getFileInfo());
		attachedFiles.stream()
			.filter(v -> reqDtlCmt.getAttachedFilesSeq() == null || !reqDtlCmt.getAttachedFilesSeq().contains(v.getFileSeq()))
			.forEach(v -> {
				fileMapper.delete(v.getFileSeq());
				fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
			});
		
		if(reqDtlCmt.getFiles() != null && !reqDtlCmt.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = reqDtlCmt.getFileInfo();
			uploadFile.setFileRealPath("request/" + reqDtlCmt.getReqSeq());
			
			reqDtlCmt.getFiles().stream()
				.forEach(v -> {
					uploadFile.setFileOrgNm(v.getOriginalFilename());
					uploadFile.setFileRealNm(FilePolicy.rename(v.getOriginalFilename()));
					fileMapper.save(uploadFile);
					
			    	log.info("fileSeq: {}", v.getOriginalFilename());
					fileStorageService.store(v, Paths.get(uploadFile.getFileRealPath()), uploadFile.getFileRealNm());
				});
			
	    	fileMapper.updateOrderNum(uploadFile);
		}
		return result;	
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtlCmt(ReqDtlCmt reqDtlCmt) {
		Optional.ofNullable(reqDtlMapper.findCommentBySeq(reqDtlCmt.getReqDtlCmtSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요구사항 코멘트를 찾을 수 없습니다."));
		
		int result = reqDtlMapper.deleteComment(reqDtlCmt);
		
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(reqDtlCmt.getFileInfo());
		
		if(attachedFiles != null && attachedFiles.size() > 0) {
			attachedFiles.stream()
				.forEach(v -> {
					fileMapper.delete(v.getFileSeq());
					fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
				});
		}
		return result;
	}

}
