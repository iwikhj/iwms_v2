package com.iwi.iwms.utils;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	public static String getCookie(HttpServletRequest request, String name) {
		if(request.getCookies() == null)
			return null;
		
		return Arrays.stream(request.getCookies())
				.filter(c -> name.equals(c.getName()))
				.map(Cookie::getValue)
				.findAny()
				.orElse(null);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setMaxAge(60 * 60 * 24 * 7);
		cookie.setHttpOnly(httpOnly);
		
		response.addCookie(cookie);
	}
	
	public static void delCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static void delAllCookies(HttpServletRequest request, HttpServletResponse response) {
		Arrays.stream(request.getCookies())
			.forEach(c -> delCookie(response, c.getName()));
	}
}