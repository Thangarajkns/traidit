package org.kns.traidit.backend.payments.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.payments.model.UserSubscriptionPayments;
import org.kns.traidit.backend.user.model.TraidItUser;

public interface PaymentDao {
	public ArrayList<UserSubscriptionPayments> getPaymentsOfUserByUser(TraidItUser user) throws PaymentNotFoundException;
	public UserSubscriptionPayments getPaymentByPaymentId(Integer paymentId) throws PaymentNotFoundException;
	public Integer saveOrUpdatePayment(UserSubscriptionPayments payment);
	public BigDecimal getSubscriptionAmountByDate(Date fromDate,Date toDate);
}
