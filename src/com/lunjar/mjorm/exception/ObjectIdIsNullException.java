package com.lunjar.mjorm.exception;

public class ObjectIdIsNullException extends Exception{
	
	private static final long serialVersionUID = 3111613255443641453L;

	public ObjectIdIsNullException() {
		super("Undefined ObjectId extends BaseVO");
	}

	public ObjectIdIsNullException(String msg) {
		super(msg);
	}
}
