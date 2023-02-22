package com.iwi.iwms.api.notice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.notice.domain.Notice;
import com.iwi.iwms.api.notice.domain.NoticeInfo;

@Mapper
public interface NoticeMapper {

	List<NoticeInfo> findAll(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	NoticeInfo findBySeq(long noticeSeq);
	
	void save(Notice notice);
	
	int update(Notice notice);
	
	int delete(Notice notice);
	
	int updateViewCnt(long noticeSeq);
}
