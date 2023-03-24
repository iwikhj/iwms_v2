package com.iwi.iwms.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.iwi.iwms.api.login.domain.LoginUserInfo;

public class PredicateMap {

	public static Map<String, Object> make(HttpServletRequest request, LoginUserInfo loginUserInfo){
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", loginUserInfo.getUserSeq());
		Enumeration<?> e = request.getParameterNames();
		while(e.hasMoreElements()){
			String name = (String) e.nextElement();
			String value = Joiner.on(",").join(request.getParameterValues(name));
			
			if(!StringUtils.hasText(value))
				continue;
			
			if(name.equals("page") || name.equals("limit"))
				continue;
			
			map.put(name, value);  
		}
		
		return map;
	}
	
	public static String camelToUnder(String table, String column){
		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		return table.concat(".").concat(column.replaceAll(regex, replacement)).toUpperCase();
	}
}
