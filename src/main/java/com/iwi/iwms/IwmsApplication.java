package com.iwi.iwms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
public class IwmsApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(IwmsApplication.class, args);
		DispatcherServlet dispatcherServlet = (DispatcherServlet)ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	}

}
