package org.kns.traidit.frontend.commission.dto;

import java.util.Date;

import org.kns.traidit.backend.commission.model.DirectDepositInformation;
import org.kns.traidit.backend.user.model.Roles;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.RolesDto;
import org.kns.traidit.frontend.user.dto.UserDto;
/**
 * 
 * @author Bhagya-KNS Technologies
 * Dto for Direct deposit Information
 * created on Jan 30th,2015
 */
public class DirectDepositInformationDto{
	private Integer depositId;
	private UserDto traiditUser;
	private String bankName;
	private String accountType;
	private String RoutingNumber;
	private String accountNumber;
	private Date  createdDate;
	private Date editedDate;
	public Integer getDepositId() {
		return depositId;
	}
	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}
	public UserDto getTraiditUser() {
		return traiditUser;
	}
	public void setTraiditUser(UserDto traiditUser) {
		this.traiditUser = traiditUser;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getRoutingNumber() {
		return RoutingNumber;
	}
	public void setRoutingNumber(String routingNumber) {
		RoutingNumber = routingNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
		
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getEditedDate() {
		return editedDate;
	}
	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}
	public static DirectDepositInformationDto populateDirectDepositInformationDto(DirectDepositInformation deposit){
		DirectDepositInformationDto depositDto=new DirectDepositInformationDto();
		depositDto.setTraiditUser(UserDto.populateUserDto(deposit.getTraiditUser()));
		depositDto.setAccountNumber(deposit.getAccountNumber());
		depositDto.setAccountType(deposit.getAccountType());
		depositDto.setBankName(deposit.getBankName());
		depositDto.setRoutingNumber(deposit.getRoutingNumber());
		depositDto.setCreatedDate(deposit.getCreatedDate());
		depositDto.setEditedDate(deposit.getEditedDate());
		depositDto.setDepositId(deposit.getDepositId());
		return depositDto;
		
	}
	
}