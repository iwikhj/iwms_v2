package com.iwi.iwms.api.notice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.file.domain.UploadFile;
import com.iwi.iwms.api.file.domain.UploadFileInfo;
import com.iwi.iwms.api.file.service.FileService;
import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;
import com.iwi.iwms.api.notice.mapper.NoticeMapper;
import com.iwi.iwms.api.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	
	private static final String UPLOAD_PATH_PREFIX = "notice/";
	
	private final NoticeMapper noticeMapper;
	
	private final FileService fileService;
	
	@Override
	public List<NoticeInfo> listNotice(Map<String, Object> map) {
		return noticeMapper.listNotice(map);
	}

	@Override
	public int countNotice(Map<String, Object> map) {
		return noticeMapper.countNotice(map);
	}

	@Override
	public NoticeInfo getNoticeBySeq(long noticeSeq) {
		return Optional.ofNullable(noticeMapper.getNoticeBySeq(noticeSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertNotice(Notice notice) {
		
		noticeMapper.insertNotice(notice);
		
		// 첨부파일 저장
		if(notice.getFiles() != null && !notice.getFiles().isEmpty()) {
			UploadFile uploadFile = notice.getFileInfo();
			uploadFile.setFileRefSeq(notice.getNoticeSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + notice.getNoticeSeq());
			fileService.insertAttachFiles(notice.getFiles(), uploadFile);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateNotice(Notice notice) {
		Optional.ofNullable(noticeMapper.getNoticeBySeq(notice.getNoticeSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
		
		int result = noticeMapper.updateNotice(notice);

		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(notice.getFileInfo());
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachFiles(attachedFiles, notice.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(notice.getFiles() != null && !notice.getFiles().isEmpty()) {
			log.info("첨부파일 있음");
			UploadFile uploadFile = notice.getFileInfo();
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + notice.getNoticeSeq());
			fileService.insertAttachFiles(notice.getFiles(), uploadFile);
		}
		return result;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteNotice(Notice notice) {
		Optional.ofNullable(noticeMapper.getNoticeBySeq(notice.getNoticeSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));
		
		int result = noticeMapper.deleteNotice(notice);
		
		// 첨부파일 삭제(디렉토리까지)
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(notice.getFileInfo());
		if(attachedFiles != null && attachedFiles.size() > 0) {
			fileService.deleteAttachAll(attachedFiles);
		}
		return result;
	}

	@Override
	public int updateViewCnt(long noticeSeq) {
		return noticeMapper.updateViewCnt(noticeSeq);
	}

}
