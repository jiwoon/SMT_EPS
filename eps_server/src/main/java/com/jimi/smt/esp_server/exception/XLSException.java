package com.jimi.smt.esp_server.exception;

/**
 * XLS解析时的错误
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class XLSException extends Exception {
	
	private String message;
	
	public XLSException(String message) {
		this.message = message;
	}
	
	public XLSException() {
		
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
