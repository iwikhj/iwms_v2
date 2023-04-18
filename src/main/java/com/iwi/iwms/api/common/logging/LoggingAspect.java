package com.iwi.iwms.api.common.logging;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Joiner;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    @Around("within(com.iwi.iwms.api.*.controller.*)")
    public Object aspectParameter(final ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String params = getRequestParams(request);
        
        long startAt = System.currentTimeMillis();
        log.info("[REQUEST] {}[{}] <method={}, path={}, param={}>", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName(), request.getMethod(), request.getRequestURI(), params);

        Object result = pjp.proceed();

        long endAt = System.currentTimeMillis();
        log.info("[RESPONSE] {}[{}] {} ({}ms)", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName(), result, endAt - startAt);

        return result;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
	      return paramMap.entrySet().stream()
	              .map(entry -> String.format("%s: %s", entry.getKey(), Joiner.on(",").join(entry.getValue())))
	              .collect(Collectors.joining(", "));
    }

	private String getRequestParams(HttpServletRequest request) {
		String params = "[]";

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
        	Map<String, String[]> paramMap = request.getParameterMap();
        	if(!paramMap.isEmpty()) {
        		params = "[" + paramMapToString(paramMap) + "]";
        	}
        }
        return params;
	}
}