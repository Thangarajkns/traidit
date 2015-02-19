package org.kns.traidit.frontend.common.exception;

public class SMSNotSentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString(){
		return "cannot send SMS";
	}
}
