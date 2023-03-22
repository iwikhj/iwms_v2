package com.iwi.iwms.api.common.errors;

import javax.ws.rs.InternalServerErrorException;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    	String message = e.getBindingResult().getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.findFirst()
				    			.get();
    	
    	return errorReturn(ErrorCode.PARAMETER_MALFORMED, message, e);

    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e){
    	String message = e.getBindingResult().getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.findFirst()
				    			.get();
    	
    	return errorReturn(ErrorCode.PARAMETER_MALFORMED, message, e);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        return errorReturn(ErrorCode.AUTHORIZATION_FAILED, null, e);
    }
    
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerErrorException(InternalServerErrorException e){
        return errorReturn(ErrorCode.INTERNAL_SERIVCE_ERROR, "Keycloak", e);
    }
    
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<?> handleRedisConnectionFailureException(RedisConnectionFailureException e){
    	 return errorReturn(ErrorCode.INTERNAL_SERIVCE_ERROR, "Redis", e);
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
    	return errorReturn(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
    
    private ResponseEntity<ErrorResponse> errorReturn(ErrorCode code, String message, Exception e) {
    	//e.printStackTrace();
    	ErrorResponse er = ErrorResponse.builder()
	    		.code(code)
	    		.message(message)
	    		.build();
    	
    	log.warn("[ERROR] {} {}", e.getClass().getName(), er.toString());
        return new ResponseEntity<ErrorResponse>(er, code.getStatus());
    }
}
