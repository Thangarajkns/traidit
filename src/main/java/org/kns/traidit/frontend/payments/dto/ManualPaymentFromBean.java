/**
 * @since 11-Feb-2015
 */
package org.kns.traidit.frontend.payments.dto;

import java.util.Date;

import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class ManualPaymentFromBean {

	private UserDto user;
	private PlansDto plan;
	private String transactionId;
	private String source = "MANUAL_PAYMENT";
	private String status = "COMPLETED";
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date accountExpirationDate;
	private String submitOption;
	private Boolean payCommissions = true;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public PlansDto getPlan() {
		return plan;
	}

	public void setPlan(PlansDto plan) {
		this.plan = plan;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAccountExpirationDate() {
		return accountExpirationDate;
	}

	public void setAccountExpirationDate(Date accountExpirationDate) {
		this.accountExpirationDate = accountExpirationDate;
	}

	public String getSubmitOption() {
		return submitOption;
	}

	public void setSubmitOption(String submitOption) {
		this.submitOption = submitOption;
	}

	
	public Boolean getPayCommissions() {
		return payCommissions;
	}

	
	public void setPayCommissions(Boolean payCommissions) {
		this.payCommissions = payCommissions;
	}

}
