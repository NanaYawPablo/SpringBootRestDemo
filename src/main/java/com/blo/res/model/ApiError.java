package com.blo.res.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

//Class for API Errors
public class ApiError{
	
	private HttpStatus status;
	private String message;
	private List<String> errors;
	private Date timeStamp;
 
    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
 
    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    
    
    
//	public Date getTimeStamp() {
//		return timeStamp;
//	}
//
//	public void setTimeStamp(Date timeStamp) {
//		this.timeStamp = timeStamp;
//	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
    
    
    
    
}
