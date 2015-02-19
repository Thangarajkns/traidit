/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.frontend.commission.dto;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.kns.traidit.backend.commission.model.CommissionOf;
import org.kns.traidit.backend.commission.model.CommissionOfActiveAccount;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class CommissionDto {
	
	private Integer Id;
	private UserDto benificiary;
	private UserDto payer;
	private UserDto sourcePayer;
	private BigDecimal commissionAmount;
	private CommissionOf commissionOf;
	private Date date;
	
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
	public UserDto getPayer() {
		return payer;
	}
	public void setPayer(UserDto payer) {
		this.payer = payer;
	}
	public BigDecimal getCommissionAmount() {
		return commissionAmount;
	}
	public UserDto getSourcePayer() {
		return sourcePayer;
	}
	public void setSourcePayer(UserDto sourcePayer) {
		this.sourcePayer = sourcePayer;
	}
	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	public CommissionOf getCommissionOf() {
		return commissionOf;
	}
	public void setCommissionOf(CommissionOf commissionOf) {
		this.commissionOf = commissionOf;
	}
	public Date getDate() {
		return date;
	}
	private void setDate(Date date) {
		this.date = date;
	}
	
	public Boolean getHasPaidCurrentMonthSubscription(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(benificiary.getAccountExpiryDate());
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		if(cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)){
			return true;
		}
		return false;
	}
	
	/**
	 * Populates all values from actual CommissionOfActiveAccount object to dto object
	 * 
	 * @param commission
	 * @return CommissionDto
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	public static CommissionDto populateCommissionDto(CommissionOfActiveAccount commission){
		CommissionDto commissionDto = new CommissionDto();
		commissionDto.setId(commission.getId());
		commissionDto.setBenificiary(UserDto.populateUserDto(commission.getbenificiary()));
		commissionDto.setPayer(UserDto.populateUserDto(commission.getpayer()));
		commissionDto.setSourcePayer(UserDto.populateUserDto(commission.getSourcePayer()));
		commissionDto.setCommissionAmount(commission.getCommissionAmount());
		commissionDto.setCommissionOf(commission.getCommissionOf());
		commissionDto.setDate(commission.getDate());
		return commissionDto;
	}
}
