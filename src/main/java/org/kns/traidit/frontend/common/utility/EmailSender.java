package org.kns.traidit.frontend.common.utility;

import org.kns.traidit.backend.commission.model.CommissionOfInActiveAccount;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.UserDto;

public interface EmailSender {
	public void sendForgotPasswordMail(final UserDto user)throws MailNotSentException;
	public void intimateLostCommission(final CommissionOfInActiveAccount commission)throws MailNotSentException;
}
