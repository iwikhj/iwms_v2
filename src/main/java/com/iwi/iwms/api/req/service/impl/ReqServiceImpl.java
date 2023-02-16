package com.iwi.iwms.api.req.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.api.req.domain.Req;
import com.iwi.iwms.api.req.domain.ReqInfo;
import com.iwi.iwms.api.req.mapper.ReqMapper;
import com.iwi.iwms.api.req.service.ReqService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReqServiceImpl implements ReqService {
	
	private final ReqMapper reqMapper;
	@Override
	public List<ReqInfo> listReq(Map<String, Object> map) {
		return reqMapper.findAll(map);
	}

	@Override
	public int countReq(Map<String, Object> map) {
		return reqMapper.count(map);
	}

	@Override
	public ReqInfo getReqBySeq(long reqSeq) {
		return Optional.ofNullable(reqMapper.findBySeq(reqSeq))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청을 찾을 수 없습니다."));
	}

	@Override
	public void insertReq(Req req) {
		reqMapper.save(req);
	}

	@Override
	public int updateReq(Req req) {
		Optional.ofNullable(reqMapper.findBySeq(req.getRegSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청을 찾을 수 없습니다."));
		
		return reqMapper.update(req);
	}

	@Override
	public int deleteReq(Req req) {
		Optional.ofNullable(reqMapper.findBySeq(req.getRegSeq()))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청을 찾을 수 없습니다."));
		
		return reqMapper.delete(req);
	}

}
