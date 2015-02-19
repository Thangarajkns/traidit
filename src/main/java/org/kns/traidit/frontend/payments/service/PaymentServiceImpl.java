package org.kns.traidit.frontend.payments.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.kns.traidit.backend.payments.dao.PaymentDao;
import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.payments.model.UserSubscriptionPayments;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

	@Resource(name="userDao")
	private UserDao userDao;
	
	@Resource(name="paymentDao")
	private PaymentDao paymentDao;	
	
	@Override
	public UserSubscriptionPaymentsDto getPaymentById(Integer paymentId) throws PaymentNotFoundException{
		UserSubscriptionPayments payment = this.paymentDao.getPaymentByPaymentId(paymentId);
		return UserSubscriptionPaymentsDto.populateUserSubscriptionPaymentsDto(payment);
	}
	
	@Override
	public Integer saveOrUpdatePayment(UserSubscriptionPaymentsDto paymentDto) throws UserNotFoundException, PlanNotFoundException{
		UserSubscriptionPayments payment = new UserSubscriptionPayments();
		payment.setUser(this.userDao.getUserbyUserId(paymentDto.getUser().getUserId()));
		payment.setAmount(paymentDto.getAmount());
		payment.setCurrency(paymentDto.getCurrency());
		payment.setPaidDate(paymentDto.getPaidDate());
		System.out.println(paymentDto.getPlan());
		if(paymentDto.getPlan() != null)
			payment.setPlan(this.userDao.getPlanByPlanId(paymentDto.getPlan().getPlanId()));
		payment.setStatus(paymentDto.getStatus());
		payment.setSubscriptionId(paymentDto.getSubscriptionId());
		payment.setTransactionId(paymentDto.getTransactionId());
		payment.setSource(paymentDto.getSource());
		return this.paymentDao.saveOrUpdatePayment(payment);
	}

	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 25-Dec-2014
	 */
	public BigDecimal getSubscriptionAmountByDate(Date fromDate,Date toDate){
		return this.paymentDao.getSubscriptionAmountByDate(fromDate,toDate);
	}

}
