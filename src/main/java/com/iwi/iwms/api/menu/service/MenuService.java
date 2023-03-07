package com.iwi.iwms.api.menu.service;

import java.util.List;
import java.util.Map;

import com.iwi.iwms.api.user.domain.MenuInfo;

public interface MenuService {
	
	List<MenuInfo> listMenu(Map<String, Object> map);

}
