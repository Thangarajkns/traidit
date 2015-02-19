package org.kns.traidit.backend.trade.exception;

/**
 * 
 * @author Thangaraj
 * @since 13-11-2014
 *
 */
public class TradeInventoryNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		
		return "Trade inventory Not Found";
	}
}
