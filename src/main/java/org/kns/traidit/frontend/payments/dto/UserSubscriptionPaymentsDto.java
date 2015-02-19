package org.kns.traidit.frontend.payments.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;

import org.kns.traidit.backend.payments.model.UserSubscriptionPayments;
import org.kns.traidit.backend.user.model.Plans;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * Acts as a DTO for the model class UserSubscriptionPayments
 * @author Thangaraj(KNSTEK)
 * @since 30-07-2014
 *
 */
public class UserSubscriptionPaymentsDto {
	
	private Integer subscriptionId;
	private UserDto user;
	private PlansDto plan;
	private BigDecimal amount;
	private Date paidDate;
	private String transactionId;
	private String currency;
	private String status;
	private String Source;
	
	public Integer getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
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
	public Date getPaidDate() {
		return paidDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	
	/**
	 * Populates UserSubscriptionPaymentsDto Dto object from given UserSubscriptionPayments model object
	 * @param payment UserSubscriptionPayments model object
	 * @return Populated UserSubscriptionPaymentsDto Dto object
	 */
	public static UserSubscriptionPaymentsDto populateUserSubscriptionPaymentsDto(UserSubscriptionPayments payment){
		if(payment == null)
			return null;
		UserSubscriptionPaymentsDto paymentsDto = new UserSubscriptionPaymentsDto();
		paymentsDto.setSubscriptionId(payment.getSubscriptionId());
		paymentsDto.setUser(UserDto.populateUserDto(payment.getUser()));
		paymentsDto.setPlan(PlansDto.populatePlansDto(payment.getPlan()));
		paymentsDto.setAmount(payment.getAmount());
		paymentsDto.setPaidDate(payment.getPaidDate());
		paymentsDto.setTransactionId(payment.getTransactionId());
		paymentsDto.setCurrency(payment.getCurrency());
		paymentsDto.setStatus(payment.getStatus());
		paymentsDto.setSource(payment.getSource());
		return paymentsDto;
	}
	
	/**
	 * Populates list of UserSubscriptionPaymentsDto Dto objects from given list of UserSubscriptionPayments model objects
	 * @param payment ArrayList of UserSubscriptionPayments model objects
	 * @return Populated ArrayList of UserSubscriptionPaymentsDto Dto objects
	 */
	public static ArrayList<UserSubscriptionPaymentsDto> populateUserSubscriptionPaymentsDtos(ArrayList<UserSubscriptionPayments> payments){
		ArrayList<UserSubscriptionPaymentsDto> paymentDtos = new ArrayList<UserSubscriptionPaymentsDto>();
		for (UserSubscriptionPayments userSubscriptionPayments : payments) {
			paymentDtos.add(populateUserSubscriptionPaymentsDto(userSubscriptionPayments));
		}
		return paymentDtos;
	}
}
