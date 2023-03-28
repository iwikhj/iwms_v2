package com.iwi.iwms.api.common.errors;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<?> handleCommonException(CommonException e) {
        return errorReturn(e.getCode(), e.getReason(), e);
    }
    
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
    	return errorReturn(ErrorCode.API_NOT_EXISTS, e.getMessage(), e);
	}
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	return errorReturn(ErrorCode.METHOD_NOT_ALLOWED, e.getMessage(), e);
	}
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    	String message = e.getBindingResult().getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.findAny()
				    			.get();
    	
    	return errorReturn(ErrorCode.PARAMETER_MALFORMED, message, e);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e){
    	String message = e.getBindingResult().getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.findAny()
				    			.get();
    	
    	return errorReturn(ErrorCode.PARAMETER_MALFORMED, message, e);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAccessDeniedException(AuthenticationException e){
        return errorReturn(ErrorCode.AUTHENTICATION_FAILED, e.getMessage(), e);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        return errorReturn(ErrorCode.AUTHORIZATION_FAILED, e.getMessage(), e);
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
    	e.printStackTrace();
    	return errorReturn(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
    
    private ResponseEntity<ErrorResponse> errorReturn(ErrorCode code, String message, Exception e) {
    	ErrorResponse er = ErrorResponse.builder()
	    		.code(code)
	    		.message(message)
	    		.build();
    	
    	log.warn("[ERROR] {} {}", e.getClass().getName(), er.toString());
        return new ResponseEntity<ErrorResponse>(er, code.getStatus());
    }
}
