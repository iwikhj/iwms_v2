package com.iwi.iwms.api.file.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.mapper.FileMapper;
import com.iwi.iwms.api.file.service.FileService;
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
		return fileMapper.listFileByRef(uploadFile);
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertAttachFiles(List<MultipartFile> multipartFiles, UploadFile uploadFile) {
		multipartFiles.stream()
			.forEach(multipartFile -> {
				File file = fileStorageService.store(multipartFile, Paths.get(uploadFile.getFileRealPath()));
				uploadFile.setFileOrgNm(multipartFile.getOriginalFilename());
				uploadFile.setFileRealNm(file.getName());
				fileMapper.insertFile(uploadFile);
			});
	
		fileMapper.updateOrderNum(uploadFile);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deleteAttachFiles(List<UploadFileInfo> oldAttachedFiles, List<Long> currentAttachedFilesSeq) {
		oldAttachedFiles.stream()
			.filter(v -> currentAttachedFilesSeq == null || !currentAttachedFilesSeq.contains(v.getFileSeq()))
			.forEach(v -> {
				fileMapper.deleteFile(v.getFileSeq());
				fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
			});
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deleteAttachAll(List<UploadFileInfo> attachedFiles) {
		attachedFiles.stream()
			.forEach(v -> {
				fileMapper.deleteFile(v.getFileSeq());
			});
		fileStorageService.deleteAll(Paths.get(attachedFiles.get(0).getFileRealPath()));
	}
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void deletePath(Path path) {
		fileStorageService.deleteAll(path);
	}
	
	@Override
	public File upload(MultipartFile multipartFile, Path path) {
		return fileStorageService.store(multipartFile, path);
	}
	
	@Override
	public UploadFileInfo getFileBySeq(long fileSeq) {
		return Optional.ofNullable(fileMapper.getFileBySeq(fileSeq))
					.orElseThrow(() -> new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일을 찾을 수 없습니다."));
	}

	@Override
	public Resource getFileResource(Path path) {
		return fileStorageService.loadAsResource(path);
	}

}
