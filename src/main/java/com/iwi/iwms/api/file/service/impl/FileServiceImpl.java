package com.iwi.iwms.api.file.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
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
	public List<UploadFileInfo> listFileByRef(UploadFile uploadFile) {
		return fileMapper.findAllByRef(uploadFile);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertAttachFiles(List<MultipartFile> multipartFiles, UploadFile uploadFile) {
		multipartFiles.stream()
			.forEach(multipartFile -> {
				uploadFile.setFileOrgNm(multipartFile.getOriginalFilename());
				uploadFile.setFileRealNm(FilePolicy.rename(multipartFile.getOriginalFilename()));
				
				fileMapper.save(uploadFile);
				fileStorageService.store(multipartFile, Paths.get(uploadFile.getFileRealPath()), uploadFile.getFileRealNm());
			});
	
		fileMapper.updateOrderNum(uploadFile);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deleteAttachFiles(List<UploadFileInfo> oldAttachedFiles, List<Long> currentAttachedFilesSeq) {
		oldAttachedFiles.stream()
			.filter(v -> currentAttachedFilesSeq == null || !currentAttachedFilesSeq.contains(v.getFileSeq()))
			.forEach(v -> {
				fileMapper.delete(v.getFileSeq());
				fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
			});
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deleteAttachAll(List<UploadFileInfo> attachedFiles) {
		attachedFiles.stream()
			.forEach(v -> {
				fileMapper.delete(v.getFileSeq());
			});
		fileStorageService.deleteAll(Paths.get(attachedFiles.get(0).getFileRealPath()));
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deletePath(Path path) {
		fileStorageService.deleteAll(path);
	}
	
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
