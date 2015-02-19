package org.kns.traidit.frontend.payments.service;

import java.math.BigDecimal;
import java.util.Date;

import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;

public interface PaymentService {

	public UserSubscriptionPaymentsDto getPaymentById(Integer paymentId) throws PaymentNotFoundException;

	public Integer saveOrUpdatePayment(UserSubscriptionPaymentsDto payment) throws UserNotFoundException, PlanNotFoundException;

	public BigDecimal getSubscriptionAmountByDate(Date fromDate,Date toDate);

}
