/*
package com.iwi.iwms.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
	
	public static String getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}

	public static String getProperty(String propertyName, String defaultValue) {
		String propertyValue = applicationContext.getEnvironment().getProperty(propertyName);
		
		if (propertyValue == null) {
			propertyValue = defaultValue == null ? "" : defaultValue;
			//System.out.println(propertyName + " properties was not loaded.");
		}
		return propertyValue;
	}

}
*/