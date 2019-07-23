package com.hit.exception;

public class UnknownIdException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public UnknownIdException(Throwable err, String message) {
		super(message, err);
	}
	
	public UnknownIdException(Throwable err) {
		super(err);
	}
}