package com.iwi.iwms.api.notice.service.impl;

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
import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;
import com.iwi.iwms.api.notice.mapper.NoticeMapper;
import com.iwi.iwms.api.notice.service.NoticeService;
import com.iwi.iwms.filestorage.service.FileStorageService;
import com.iwi.iwms.utils.FilePolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	
	private final NoticeMapper noticeMapper;
	
	private final FileMapper fileMapper;
	
    private final FileStorageService fileStorageService;
	
	@Override
	public List<NoticeInfo> listNotice(Map<String, Object> map) {
		return noticeMapper.findAll(map);
	}

	@Override
	public int countNotice(Map<String, Object> map) {
		return noticeMapper.count(map);
	}

	@Override
	public NoticeInfo getNoticeBySeq(long noticeSeq) {
		return Optional.ofNullable(noticeMapper.findBySeq(noticeSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertNotice(Notice notice) {
		
		noticeMapper.save(notice);
		log.info("noticeSeq: {}, 첨부파일: {}", notice.getNoticeSeq(), notice.getFiles());
		
		if(notice.getFiles() != null && !notice.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = notice.getFileInfo();
			uploadFile.setFileRefSeq(notice.getNoticeSeq());
			uploadFile.setFileRealPath("notice/" + notice.getNoticeSeq());
			
			notice.getFiles().stream()
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
	public int updateNotice(Notice notice) {
		Optional.ofNullable(noticeMapper.findBySeq(notice.getNoticeSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
		
		int result = noticeMapper.update(notice);
		//
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(notice.getFileInfo());
		attachedFiles.stream()
			.filter(v -> notice.getAttachedFilesSeq() == null || !notice.getAttachedFilesSeq().contains(v.getFileSeq()))
			.forEach(v -> {
				fileMapper.delete(v.getFileSeq());
				fileStorageService.delete(Paths.get(v.getFileRealPath()).resolve(v.getFileRealNm()));
			});
		
		if(notice.getFiles() != null && !notice.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = notice.getFileInfo();
			uploadFile.setFileRealPath("notice/" + notice.getNoticeSeq());
			
			notice.getFiles().stream()
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
	public int deleteNotice(Notice notice) {
		Optional.ofNullable(noticeMapper.findBySeq(notice.getNoticeSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
		
		int result = noticeMapper.delete(notice);
		
		List<UploadFileInfo> attachedFiles = fileMapper.findAll(notice.getFileInfo());
		
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
	public int updateViewCnt(long noticeSeq) {
		return noticeMapper.updateViewCnt(noticeSeq);
	}

}
