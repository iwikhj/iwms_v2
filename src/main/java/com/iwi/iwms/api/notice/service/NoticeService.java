package com.iwi.iwms.api.notice.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;

public interface NoticeService {

	List<NoticeInfo> listNotice(Map<String, Object> map);
	
	int countNotice(Map<String, Object> map);

	NoticeInfo getNoticeBySeq(long noticeSeq, long userSeq);
	
	void insertNotice(Notice notice);
	
	int updateNotice(Notice notice);
	
	int deleteNotice(Notice notice);

	int updateViewCnt(long noticeSeq);
}
