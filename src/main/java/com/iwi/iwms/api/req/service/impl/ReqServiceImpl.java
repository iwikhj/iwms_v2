package com.iwi.iwms.api.req.service.impl;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.mapper.FileMapper;
import com.iwi.iwms.api.notice.service.impl.NoticeServiceImpl;
import com.iwi.iwms.api.req.domain.Agree;
import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqService;
import com.iwi.iwms.filestorage.service.FileStorageService;
import com.iwi.iwms.utils.FilePolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqServiceImpl implements ReqService {
	
	private final ReqMapper reqMapper;
	
	private final FileMapper fileMapper;
	
    private final FileStorageService fileStorageService;
    
	@Override
	public List<ReqInfo> listReq(Map<String, Object> map) {
		return reqMapper.findAll(map);
	}

	@Override
	public int countReq(Map<String, Object> map) {
		return reqMapper.count(map);
	}

	@Override
	public ReqInfo getReqBySeq(long reqSeq) {
		return Optional.ofNullable(reqMapper.findBySeq(reqSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
	}

	@Override
	public void insertReq(Req req) {
	
		reqMapper.save(req);
		log.info("reqSeq: {}, 첨부파일: {}", req.getReqSeq(), req.getFiles());
		
		if(req.getFiles() != null && !req.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = req.getFileInfo();
			uploadFile.setFileRefSeq(req.getReqSeq());
			uploadFile.setFileRealPath("request/" + req.getReqSeq());
			
			req.getFiles().stream()
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

	@Override
	public int updateReq(Req req) {
		Optional.ofNullable(reqMapper.findBySeq(req.getRegSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		int result = reqMapper.update(req);
		
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(req.getFileInfo());
		attachedFiles.stream()
			.filter(v -> req.getAttachedFilesSeq() == null || !req.getAttachedFilesSeq().contains(v.getFileSeq()))
			.forEach(v -> {
				fileMapper.delete(v.getFileSeq());
				fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
			});
		
		if(req.getFiles() != null && !req.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = req.getFileInfo();
			uploadFile.setFileRealPath("request/" + req.getReqSeq());
			
			req.getFiles().stream()
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
	public int deleteReq(Req req) {
		Optional.ofNullable(reqMapper.findBySeq(req.getRegSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항을 찾을 수 없습니다."));
		
		int result = reqMapper.delete(req);
		
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(req.getFileInfo());
		
		if(attachedFiles != null && attachedFiles.size() > 0) {
			attachedFiles.stream()
				.forEach(v -> {
					fileMapper.delete(v.getFileSeq());
				});
			
			fileStorageService.deleteAll(Paths.get(attachedFiles.get(0).getFileRealPath()));
		}
		return result;
	}
	
	@Override
	public int updateReqAgree(Agree agree) {
		return reqMapper.updateAgree(agree);
	}

}
