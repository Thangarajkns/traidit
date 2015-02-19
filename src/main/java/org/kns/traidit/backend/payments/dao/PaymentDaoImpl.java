package org.kns.traidit.backend.payments.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.criteria.predicate.IsEmptyPredicate;
import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.payments.model.UserSubscriptionPayments;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository("paymentDao")
@Transactional
public class PaymentDaoImpl implements PaymentDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ArrayList<UserSubscriptionPayments> getPaymentsOfUserByUser(TraidItUser user) throws PaymentNotFoundException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(UserSubscriptionPayments.class);
		criteria.add(Restrictions.eq("user", user));
		ArrayList<UserSubscriptionPayments> payments = (ArrayList<UserSubscriptionPayments>)criteria.list();
		if(payments.isEmpty()){
			throw new PaymentNotFoundException();
		}
		else{
			return payments;
		}
	}
	
	/**
	 * 
	 */
	public UserSubscriptionPayments getPaymentByPaymentId(Integer paymentId) throws PaymentNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserSubscriptionPayments.class);
		criteria.add(Restrictions.eq("subscriptionId",paymentId));
		ArrayList<UserSubscriptionPayments> payments = (ArrayList<UserSubscriptionPayments>)criteria.list();
		if(payments.isEmpty() || payments == null){
			throw new PaymentNotFoundException();
		}
		return payments.get(0);
	}
	
	
	@Override
	public Integer saveOrUpdatePayment(UserSubscriptionPayments payment){
		this.sessionFactory.getCurrentSession().saveOrUpdate(payment);
		this.sessionFactory.getCurrentSession().flush();
		return payment.getSubscriptionId();
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
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(UserSubscriptionPayments.class);
		criteria.add(Restrictions.between("paidDate", fromDate, toDate));
		criteria.setProjection(Projections.sum("amount"));
		BigDecimal subscriptionAmount = (BigDecimal)criteria.list().get(0);
		if(subscriptionAmount == null)
			subscriptionAmount = new BigDecimal(0);
		return subscriptionAmount;
	}
}
