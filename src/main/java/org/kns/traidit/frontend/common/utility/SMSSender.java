package org.kns.traidit.frontend.common.utility;

import org.kns.traidit.frontend.common.exception.SMSNotSentException;
import org.kns.traidit.frontend.user.dto.UserDto;

public interface SMSSender {
	public void sendSMS(final String phoneNo,final String message) throws SMSNotSentException;
	public void sendPasswordResetSMS(UserDto user) throws SMSNotSentException;
}
