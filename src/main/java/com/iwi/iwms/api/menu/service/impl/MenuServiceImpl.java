package com.iwi.iwms.api.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.iwi.iwms.api.menu.mapper.MenuMapper;
import com.iwi.iwms.api.menu.service.MenuService;
import com.iwi.iwms.api.user.domain.MenuInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

	private final MenuMapper menuMapper;
	
	@Override
	public List<MenuInfo> listMenu(Map<String, Object> map) {
		return menuMapper.findAll(map);
	}

}
