package com.iwi.iwms.api.file.service.impl;

import java.nio.file.Path;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.mapper.FileMapper;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.filestorage.FileStorageResponse;
import com.iwi.iwms.filestorage.service.FileStorageService;
import com.iwi.iwms.utils.FilePolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileMapper fileMapper;
	
    private final FileStorageService fileStorageService;

	@Override
	public FileStorageResponse upload(MultipartFile multipartFile, Path path) {
		return fileStorageService.store(multipartFile, path, FilePolicy.rename(multipartFile.getOriginalFilename()));
	}
	
	@Override
	public UploadFileInfo getFileBySeq(long fileSeq) {
		return Optional.ofNullable(fileMapper.findBySeq(fileSeq))
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."));
	}

	@Override
	public Resource getFileResource(Path path) {
		return fileStorageService.loadAsResource(path);
	}

}
