package com.lunjar.mjorm.exception;

/**
 *  没有定义注解集合名字异常
 * @author Ezir
 *
 */
public class UndefinedDBCollectionNameException extends Exception{
		
	private static final long serialVersionUID = 5809598738984621104L;

	public UndefinedDBCollectionNameException() {
		super("Undefined BaseVO @DBCollectionName");
	}

	public UndefinedDBCollectionNameException(String msg) {
		super(msg);
	}
}
