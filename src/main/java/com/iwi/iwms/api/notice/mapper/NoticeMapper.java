package com.iwi.iwms.api.notice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;

@Mapper
public interface NoticeMapper {

	List<NoticeInfo> listNotice(Map<String, Object> map);
	
	int countNotice(Map<String, Object> map);
	
	NoticeInfo getNoticeBySeq(long noticeSeq);
	
	void insertNotice(Notice notice);
	
	int updateNotice(Notice notice);
	
	int deleteNotice(Notice notice);
	
	int updateViewCnt(long noticeSeq);
}
