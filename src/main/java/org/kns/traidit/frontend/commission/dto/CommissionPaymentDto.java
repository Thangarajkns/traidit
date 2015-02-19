/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.frontend.commission.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.kns.traidit.backend.commission.model.CommissionPayment;
import org.kns.traidit.backend.commission.model.CommissionPaymentType;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class CommissionPaymentDto {
	
	private Integer Id;
	private UserDto benificiary;
	private BigDecimal amount;
	private Date date;
	private CommissionPaymentType paymentType;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public UserDto getBenificiary() {
		return benificiary;
	}
	public void setBenificiary(UserDto benificiary) {
		this.benificiary = benificiary;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	/**
	 * Creation Date cannot be set by user
	 * @param date
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	private void setDate(Date date) {
		this.date = date;
	}
	public CommissionPaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(CommissionPaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	/**
	 * Populates all values from actual CommissionPayment object to dto object
	 * 
	 * @param payment
	 * @return CommissionPaymentDto
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	public static CommissionPaymentDto populateCommissionPaymentDto(CommissionPayment payment){
		CommissionPaymentDto paymentDto = new CommissionPaymentDto();
		paymentDto.setId(payment.getId());
		paymentDto.setBenificiary(UserDto.populateUserDto(payment.getBenificiary()));
		paymentDto.setAmount(payment.getAmount());
		paymentDto.setPaymentType(payment.getPaymentType());
		paymentDto.setDate(payment.getDate());
		return paymentDto;
	}

}
