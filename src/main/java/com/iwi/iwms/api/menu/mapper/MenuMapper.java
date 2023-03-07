package com.iwi.iwms.api.menu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.iwi.iwms.api.user.domain.MenuInfo;

@Mapper
public interface MenuMapper {

	List<MenuInfo> findAll(Map<String, Object> map);
}
