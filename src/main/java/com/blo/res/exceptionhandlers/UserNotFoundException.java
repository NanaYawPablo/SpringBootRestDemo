package com.blo.res.exceptionhandlers;

//RuntimeException class
public class UserNotFoundException extends RuntimeException {
	/** Default Serial Version UID*/
	private static final long serialVersionUID = 1L;

	//constructor
	public UserNotFoundException() {
	    super("User not found");
	  }
	
}
