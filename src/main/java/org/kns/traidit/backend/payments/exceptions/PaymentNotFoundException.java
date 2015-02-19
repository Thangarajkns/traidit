package org.kns.traidit.backend.payments.exceptions;

public class PaymentNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Payment not found";
	}

}
