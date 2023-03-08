package com.iwi.iwms.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
	
	public static String getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}

	public static String getProperty(String propertyName, String defaultValue) {
		String value = defaultValue;
		if (applicationContext.getEnvironment().getProperty(propertyName) == null) {
			//propertyName + " properties was not loaded."
		} else {
			value = applicationContext.getEnvironment().getProperty(propertyName).toString();
		}
		return value;
	}

}
