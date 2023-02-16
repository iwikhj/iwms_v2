package com.iwi.iwms.api.file.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.mapper.FileMapper;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.filestorage.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileMapper fileMapper;
	
    private final FileStorageService fileStorageService;
	
	@Override
	public List<UploadFileInfo> listUploadFile(Map<String, Object> map) {
		return fileMapper.findAll(map);
	}

	@Override
	public int countUploadFile(Map<String, Object> map) {
		return fileMapper.count(map);
	}

	@Override
	public UploadFileInfo getUploadFileInfoBySeq(long fileSeq) {
		return Optional.ofNullable(fileMapper.findBySeq(fileSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public UploadFileInfo insertUploadFile(UploadFile uploadFile) {
    	fileMapper.save(uploadFile);
    	fileMapper.updateOrderNum(uploadFile);
    	
    	fileStorageService.store(uploadFile.getMultipartFile(), uploadFile.getFileRealPath(), uploadFile.getFileRealNm());
    	
    	return fileMapper.findBySeq(uploadFile.getFileSeq());
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteUploadFile(UploadFile uploadFile) {
		UploadFileInfo uploadFileInfo = Optional.ofNullable(fileMapper.findBySeq(uploadFile.getFileSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
		
		UploadFile upFile = UploadFile.of(uploadFileInfo);
		//upFile.setRegSeq(uploadFile.getRegSeq());
		//upFile.setUpdtSeq(uploadFile.getUpdtSeq());
		
		if(fileMapper.delete(upFile) > 0) {
	    	fileMapper.updateOrderNum(upFile);
	    	
			Path path = Paths.get(upFile.getFileRealPath()).resolve(upFile.getFileRealNm());
			fileStorageService.delete(path);
			return 1;
		}
		
		return 0;
	}

	@Override
	public Resource getUploadFileResource(Path path) {
		return fileStorageService.loadAsResource(path);
	}

}
