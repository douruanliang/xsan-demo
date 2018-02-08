package org.security.exception;

import java.util.HashMap;
import java.util.Map;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -1664253452080335749L;
	private int type;
	private Map<String, Object> attributes = new HashMap<String, Object>();

	public BusinessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusinessException(String message, int type) {
		super(message);
		this.type = type;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void putAttributes(String key, Object value) {
		this.attributes.put(key, value);
	}

	public Object getAttributes(String key) {
		return this.attributes.get(key);
	}

}