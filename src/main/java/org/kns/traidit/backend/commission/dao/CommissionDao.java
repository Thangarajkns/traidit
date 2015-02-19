/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

/**
 * @author Thangaraj
 *
 */
public interface CommissionDao {
	public Integer saveOrUpdateCommissionOfInActiveAccount(CommissionOfInActiveAccount commission) throws CommissionNotSavedException;
	public Integer saveOrUpdateCommissionOfActiveAccount(CommissionOfActiveAccount commission) throws CommissionNotSavedException;
	public Integer saveOrUpdateCommissionPayment(CommissionPayment payment) throws CommissionPaymentNotSavedException;
	public BigDecimal getTotalCommissionEarned(TraidItUser user);
	public BigDecimal getTotalCommissionPaid(TraidItUser user);
	public BigDecimal getCommissionByDate(Date fromDate,Date toDate);
	public ArrayList<CommissionOfActiveAccount> getCommissionsOfAllUsers(String sortBy,String sortOrder,Integer limit,Integer startIndex);
	public ArrayList<CommissionPayment> getPaymentsOfUser(Integer userId,String sortBy,String sortOrder,Integer limit,Integer startIndex);
	public ArrayList<CommissionOfActiveAccount> getCommissionsOfUser(Integer userId) throws UserNotFoundException;
	public Integer updatePrivacyPolicy(PrivacyPolicy privacyPolicy);
	public PrivacyPolicy getPrivacyPolicy(TermsAndPrivacyPolicy type) throws PrivacyPolicyNotFoundException;
	public Map<Integer, BigDecimal> getCommissionPaymentsOfAllUsersBetweenDays(Date startDate,Date endDate) throws CommissionPaymentsNotFoundException;
	public Map<Integer, BigDecimal> getCommissionsOfAllUsersBetweenDays(Date startDate,Date endDate) throws CommissionsNotFoundException;

}
