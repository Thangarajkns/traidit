/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.dao;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.kns.traidit.backend.commission.exception.CommissionNotSavedException;
import org.kns.traidit.backend.commission.exception.CommissionPaymentNotSavedException;
import org.kns.traidit.backend.commission.exception.PrivacyPolicyNotFoundException;
import org.kns.traidit.backend.commission.model.CommissionOfActiveAccount;
import org.kns.traidit.backend.commission.model.CommissionOfInActiveAccount;
import org.kns.traidit.backend.commission.model.CommissionPayment;
import org.kns.traidit.backend.commission.model.PrivacyPolicy;
import org.kns.traidit.backend.commission.model.TermsAndPrivacyPolicy;
import org.kns.traidit.backend.user.exception.CommissionPaymentsNotFoundException;
import org.kns.traidit.backend.user.exception.CommissionsNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj
 *
 */
@Repository("commissionDao")
@Transactional
public class CommissionDaoImpl implements CommissionDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger log = Logger.getLogger(CommissionDaoImpl.class);

	public static Integer totalNoOfResults; 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * saves the given CommissionOfInActiveAccount object to db and returns the id of saved object
	 * 
	 * @param commission
	 * @throws CommissionNotSavedException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	@Override
	public Integer saveOrUpdateCommissionOfInActiveAccount(CommissionOfInActiveAccount commission) throws CommissionNotSavedException{
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(commission);
			sessionFactory.getCurrentSession().flush();
			return commission.getId();
		}
		catch(Exception e){
			throw new CommissionNotSavedException("Could not save Commission Of InActive Account");
		}
	}

	/**
	 *  saves the given CommissionOfActiveAccount object to db and returns the id of saved object
	 *  
	 * @param commission
	 * @throws CommissionNotSavedException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	@Override
	public Integer saveOrUpdateCommissionOfActiveAccount(CommissionOfActiveAccount commission) throws CommissionNotSavedException{
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(commission);
			sessionFactory.getCurrentSession().flush();
			return commission.getId();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommissionNotSavedException("Could not save Commission Of Active Account");
		}
	}
	
	/**
	 *  saves the given CommissionPayment object to db and returns the id of saved object
	 *  
	 * @param payment
	 * @throws CommissionPaymentNotSavedException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	@Override
	public Integer saveOrUpdateCommissionPayment(CommissionPayment payment) throws CommissionPaymentNotSavedException{
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(payment);
			sessionFactory.getCurrentSession().flush();
			return payment.getId();
		}
		catch(Exception e){
			throw new CommissionPaymentNotSavedException("Could not save Commission Payment");
		}
	}
		
	/**
	 * 
	 * @param user
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Dec-2014
	 */
	public BigDecimal getTotalCommissionEarned(TraidItUser user){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionOfActiveAccount.class);
		criteria.createAlias("benificiary", "b");
		criteria.add(Restrictions.eq("b.userId", user.getUserId()));
		criteria.setProjection(Projections.sum("commissionAmount"));
		ArrayList<Object> result = (ArrayList<Object>)criteria.list();
		BigDecimal totalCommissionEarned = (BigDecimal)result.get(0);
		totalCommissionEarned = totalCommissionEarned == null?new BigDecimal(0):totalCommissionEarned;
		return totalCommissionEarned;
	}
	
	/**
	 * 
	 * @param user
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Dec-2014
	 */
	public BigDecimal getTotalCommissionPaid(TraidItUser user){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionPayment.class);
		criteria.createAlias("benificiary", "b");
		criteria.add(Restrictions.eq("b.userId", user.getUserId()));
		criteria.setProjection(Projections.sum("amount"));
		ArrayList<Object> result = (ArrayList<Object>)criteria.list();
		BigDecimal totalCommissionPaid = (BigDecimal)result.get(0);
		totalCommissionPaid = totalCommissionPaid == null?new BigDecimal(0):totalCommissionPaid;
		return totalCommissionPaid;
	}

	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 17-Dec-2014
	 */
	public BigDecimal getCommissionByDate(Date fromDate,Date toDate){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionOfActiveAccount.class);
		criteria.add(Restrictions.between("date", fromDate, toDate));
		criteria.setProjection(Projections.sum("commissionAmount"));
		ArrayList<Object> result = (ArrayList<Object>)criteria.list();
		BigDecimal totalCommission = (BigDecimal)result.get(0);
		totalCommission = totalCommission == null?new BigDecimal(0):totalCommission;
		return totalCommission;
	}

	public ArrayList<CommissionOfActiveAccount> getCommissionsOfAllUsers(String sortBy,String sortOrder,Integer limit,Integer startIndex){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionOfActiveAccount.class);
		
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("benificiary")).add(Projections.sum("commissionAmount")));
		if(sortBy== null || sortBy.isEmpty()||sortBy.equals("username"))
			criteria.createAlias("benificiary", "benificiary");
			sortBy = "benificiary.firstName";
		if(sortOrder != null && sortOrder.equals("ASC"))
			criteria.addOrder(Order.asc(sortBy));
		else
			criteria.addOrder(Order.desc(sortBy));
		totalNoOfResults = criteria.list().size();
		if(limit != null && limit != 0)
			criteria.setMaxResults(limit);
		if(startIndex != null && startIndex != 0)
			criteria.setFirstResult(startIndex);
		ArrayList<Object[]> commissions = (ArrayList<Object[]>)criteria.list();
		ArrayList<CommissionOfActiveAccount> commissionList = new ArrayList<CommissionOfActiveAccount>();
		for(Object[] obj : commissions){
			CommissionOfActiveAccount commission = new CommissionOfActiveAccount();
			commission.setbenificiary((TraidItUser)obj[0]);
			commission.setCommissionAmount((BigDecimal)obj[1]);
			commissionList.add(commission);
		}
		return commissionList;
	}

	public ArrayList<CommissionPayment> getPaymentsOfUser(Integer userId,String sortBy,String sortOrder,Integer limit,Integer startIndex){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionPayment.class);
		criteria.add(Restrictions.eq("benificiary.userId", userId));
		if(sortBy== null || sortBy.isEmpty())
			sortBy = "date";
		if(sortOrder != null && sortOrder.equals("ASC"))
			criteria.addOrder(Order.asc(sortBy));
		else
			criteria.addOrder(Order.desc(sortBy));
		totalNoOfResults = criteria.list().size();
		if(limit != null && limit != 0)
			criteria.setMaxResults(limit);
		if(startIndex != null && startIndex != 0)
			criteria.setFirstResult(startIndex);
		@SuppressWarnings("unchecked")
		ArrayList<CommissionPayment> payments = (ArrayList<CommissionPayment>)criteria.list();
		return payments;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @return ArrayList<CommissionOfActiveAccount>
	 * @author Bhagya
	 * @since 05-Jan-2015
	 * 
	 * 	Method For Getting All commissions Based On UserId from CommissionOfActiveAccount Table
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CommissionOfActiveAccount> getCommissionsOfUser(Integer userId) throws UserNotFoundException{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(CommissionOfActiveAccount.class);
		criteria.add(Restrictions.eq("benificiary.userId", userId));
		ArrayList<CommissionOfActiveAccount> commissions=(ArrayList<CommissionOfActiveAccount>)criteria.list();
		return commissions;
		
	}
	/**
	 * Created By Bhagya On Feb 17th,2015
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws CommissionPaymentsNotFoundException
	 * 
	 * Method For getting the all commissionPayments of all users between startdate and end date
	 * 1.we applied restrictions as startdate and enddate to commissionpaidate
	 * 2.we are grouping the traidit users and then sum the commissionamount of user
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, BigDecimal> getCommissionPaymentsOfAllUsersBetweenDays(Date startDate,Date endDate) throws CommissionPaymentsNotFoundException {
		Map<Integer, BigDecimal> commissionsEarned = new HashMap<Integer, BigDecimal>();
		log.info("inside getCommissionPaymentsOfAllUsersBetweenDays");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionPayment.class)
								.add(Restrictions.between("date", startDate, endDate));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("benificiary.userId")).add(Projections.sum("amount")));
		ArrayList<Object[]> payments = (ArrayList<Object[]>) criteria.list();
		if (!payments.isEmpty()) {
			for(Object[] payment:payments){
				commissionsEarned.put((Integer)payment[0], (BigDecimal)payment[1]);
			}
				return commissionsEarned;
		} 
		else {
			throw new CommissionPaymentsNotFoundException();
		}
		
	}
	/**
	 * Created By Bhagya On Feb 17th,2015
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws CommissionsNotFoundException
	 * 
	 * Method For getting all commissions of all users Between startdate and enddates
	 * 1.we applied restrictions as startdate and enddate to commissionpaidate
	 * 2.we are grouping the traidit users and then sum the commissionamount of user
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, BigDecimal> getCommissionsOfAllUsersBetweenDays(Date startDate,Date endDate) throws CommissionsNotFoundException{
		Map<Integer, BigDecimal> commissionsEarned = new HashMap<Integer, BigDecimal>();
		log.info("inside getCommissionsOfAllUsersBetweenDays()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CommissionOfActiveAccount.class);
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("benificiary.userId")).add(Projections.sum("commissionAmount")));
		ArrayList<Object[]> payments = (ArrayList<Object[]>) criteria.list();
		if (!payments.isEmpty()) {
			for(Object[] payment:payments)
			{
				commissionsEarned.put((Integer)payment[0], (BigDecimal)payment[1]);
			}
			
			return commissionsEarned;
		}
		else {
			throw new CommissionsNotFoundException();
		}
	}
	/**
	 * 
	 * @param privacyPolicy
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Feb-2015
	 */
	@Override
	public Integer updatePrivacyPolicy(PrivacyPolicy privacyPolicy){
		sessionFactory.getCurrentSession().saveOrUpdate(privacyPolicy);
		sessionFactory.getCurrentSession().flush();
		return privacyPolicy.getId();
	}
	
	/**
	 * 
	 * @throws PrivacyPolicyNotFoundException
	 * @return PrivacyPolicy
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Feb-2015
	 */
	@Override
	public PrivacyPolicy getPrivacyPolicy(TermsAndPrivacyPolicy type) throws PrivacyPolicyNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PrivacyPolicy.class);
		criteria.add(Restrictions.eq("type", type.toString()));
		ArrayList<PrivacyPolicy> list = (ArrayList<PrivacyPolicy>) criteria.list();
		if(list == null || list.isEmpty())
			throw new PrivacyPolicyNotFoundException();
		return list.get(0);
	}
	
}
