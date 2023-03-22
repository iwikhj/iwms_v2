package com.iwi.iwms.api.notice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
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
	public NoticeInfo getNoticeBySeq(long noticeSeq, long loginUserSeq) {
		Map<String, Object> map = new HashMap<>();
		map.put("noticeSeq", noticeSeq);
		map.put("loginUserSeq", loginUserSeq);
		
		return Optional.ofNullable(noticeMapper.getNoticeBySeq(map))
				.orElseThrow(() -> new CommonException(ErrorCode.TARGET_DATA_NOT_EXISTS, "공지사항을 찾을 수 없습니다."));
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertNotice(Notice notice) {
		
		noticeMapper.insertNotice(notice);
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(notice.getFiles())) {
			UploadFile uploadFile = notice.getFileInfo();
			uploadFile.setFileRefSeq(notice.getNoticeSeq());
			uploadFile.setFileRealPath(UPLOAD_PATH_PREFIX + notice.getNoticeSeq());
			fileService.insertAttachFiles(notice.getFiles(), uploadFile);
		}
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateNotice(Notice notice) {
		
		this.getNoticeBySeq(notice.getNoticeSeq(), notice.getLoginUserSeq());
		
		int result = noticeMapper.updateNotice(notice);

		// 첨부파일 삭제
		List<UploadFileInfo> attachedFiles = fileService.listFileByRef(notice.getFileInfo());
		if(!CollectionUtils.isEmpty(attachedFiles)) {
			fileService.deleteAttachFiles(attachedFiles, notice.getAttachedFilesSeq());
		}
		
		// 첨부파일 저장
		if(!CollectionUtils.isEmpty(notice.getFiles())) {
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
		
		this.getNoticeBySeq(notice.getNoticeSeq(), notice.getLoginUserSeq());
		
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
