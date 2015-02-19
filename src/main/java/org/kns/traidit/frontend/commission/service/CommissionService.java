/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.frontend.commission.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.kns.traidit.backend.commission.exception.CommissionNotSavedException;
import org.kns.traidit.backend.commission.exception.CommissionPaymentNotSavedException;
import org.kns.traidit.backend.commission.model.TermsAndPrivacyPolicy;
import org.kns.traidit.backend.user.exception.CommissionPaymentsNotFoundException;
import org.kns.traidit.backend.user.exception.CommissionsNotFoundException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.CommissionDto;
import org.kns.traidit.frontend.commission.dto.CommissionPaymentDto;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface CommissionService {
	public void sendCommissionToReferer(UserDto payerDto, BigDecimal amount) throws UserNotFoundException,
			CommissionNotSavedException;

	public BigDecimal getCommissionByUser(UserDto userDto);

	public BigDecimal getCommissionByDate(Date fromDate, Date toDate);

	public Boolean paySubscriptionFromCommission(UserDto userDto) throws CommissionPaymentNotSavedException,
			UserNotFoundException, CommissionNotSavedException, PlanNotFoundException, PlanNotFoundException;

	public ArrayList<CommissionDto> getCommissionsOfAllUsers(String sortBy, String sortOrder, Integer limit,
			Integer startIndex);

	public ArrayList<CommissionPaymentDto> getPaymentsOfUser(Integer userId, String sortBy, String sortOrder,
			Integer limit, Integer startIndex);

	public ArrayList<CommissionDto> getCommissionsOfUser(Integer userId) throws UserNotFoundException;

	public BigDecimal getRevenueAmountByDate(Date fromDate, Date toDate);

	public Integer updatePrivacyPolicy(String privacyPolicy, TermsAndPrivacyPolicy type);

	public String getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy type);

	public ArrayList<CommissionDto> getMonthlyCommissionsOfAllUsers() throws CommissionsNotFoundException,
			UserNotFoundException;

	public ArrayList<CommissionDto> getCommissionsFromStartOfYearToToday() throws CommissionsNotFoundException,
			UserNotFoundException;

}
