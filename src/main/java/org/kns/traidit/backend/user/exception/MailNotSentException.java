package org.kns.traidit.backend.user.exception;

public class MailNotSentException extends Exception {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "Mail not sent Exception";
	}
}
