package com.iwi.iwms.api.common.errors;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<?> handleResponseStatusException(ResponseStatusException e, HttpServletRequest request) {
        return setErrorResponse(request, e.getStatus(), e.getReason(), e);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
    	BindingResult bindingResult = e.getBindingResult();
    	String message = bindingResult.getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.findFirst()
				    			.get();
    	
        return setErrorResponse(request, HttpStatus.BAD_REQUEST, message, e);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e, HttpServletRequest request){
    	BindingResult bindingResult = e.getBindingResult();
    	String message = bindingResult.getFieldErrors().stream()
				    			.map(v -> v.getDefaultMessage())
				    			.collect(Collectors.joining(", "));
    	
        return setErrorResponse(request, HttpStatus.BAD_REQUEST, message, e);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return setErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        return setErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
    
    private ResponseEntity<ErrorResponse> setErrorResponse(HttpServletRequest request, HttpStatus status, String message, Exception e) {
    	//e.printStackTrace();
        ResponseEntity<ErrorResponse> response =  new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
       		 .request(request)
       		 .status(status.value())
       		 .message(message)
       		 .build(), status);
        
        log.info("RESPONSE: {} {}", e.getClass().getName(), response.toString());
        
        return response;
    }
}
