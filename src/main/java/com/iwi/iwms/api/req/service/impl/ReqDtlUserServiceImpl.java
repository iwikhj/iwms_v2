package com.iwi.iwms.api.req.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.req.domain.ReqDtlUser;
import com.iwi.iwms.api.req.mapper.ReqDtlMapper;
import com.iwi.iwms.api.req.mapper.ReqDtlUserMapper;
import com.iwi.iwms.api.req.service.ReqDtlUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqDtlUserServiceImpl implements ReqDtlUserService {
	
	private final ReqDtlMapper reqDtlMapper;
	
	private final ReqDtlUserMapper reqDtlUserMapper;

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public void insertReqDtlUser(ReqDtlUser reqDtlUser) {
		Optional.ofNullable(reqDtlMapper.getReqDtlBySeq(reqDtlUser.getReqDtlSeq()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 상세를 찾을 수 없습니다."));
		
		reqDtlUserMapper.insertReqDtlUser(reqDtlUser);
		reqDtlUserMapper.insertReqDtlUserCmt(reqDtlUser);
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int updateReqDtlUser(ReqDtlUser reqDtlUser) {
		Optional.ofNullable(reqDtlUserMapper.getReqDtlUserBySeq(reqDtlUser.getReqDtlUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 상세 담당자 코멘트를 찾을 수 없습니다."));
		
		reqDtlUserMapper.updateReqDtlUser(reqDtlUser);
		reqDtlUserMapper.updateReqDtlUserCmt(reqDtlUser);
		
		return 1;
	}

	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int deleteReqDtlUser(ReqDtlUser reqDtlUser) {
		Optional.ofNullable(reqDtlUserMapper.getReqDtlUserBySeq(reqDtlUser.getReqDtlUserSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청사항 상세 담당자 코멘트를 찾을 수 없습니다."));
		
		return reqDtlUserMapper.deleteReqDtlUser(reqDtlUser);
	}

	

}
