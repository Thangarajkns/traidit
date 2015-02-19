package org.kns.traidit.frontend.commission.dto;

import java.util.Date;

import org.kns.traidit.backend.commission.model.TaxInformation;
import org.kns.traidit.frontend.user.dto.UserDto;
/**
 * 
 * @author Bhagya-Kns Technologies
 * Dto For TaxInformation
 * Created On JAn 30th,2015
 *
 */
public class TaxInformationDto{
	private Integer taxId;
	private UserDto traiditUser;
	private String socialSecurityNumber;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date dateOfBirth;
	private Boolean termsAndConditions;
	private Date createdDate;
	private Date editedDate;
	public Integer getTaxId() {
		return taxId;
	}
	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}
	public UserDto getTraiditUser() {
		return traiditUser;
	}
	public void setTraiditUser(UserDto traiditUser) {
		this.traiditUser = traiditUser;
	}
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
		
	public Boolean getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
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
	public static TaxInformationDto popTaxInformationDto(TaxInformation taxInfo){
		TaxInformationDto taxInfoDto=new TaxInformationDto();
		taxInfoDto.setTraiditUser(UserDto.populateUserDto(taxInfo.getTraiditUser()));
		taxInfoDto.setSocialSecurityNumber(taxInfo.getSocialSecurityNumber());
		taxInfoDto.setFirstName(taxInfo.getFirstName());
		taxInfoDto.setMiddleName(taxInfo.getMiddleName());
		taxInfoDto.setLastName(taxInfo.getLastName());
		taxInfoDto.setDateOfBirth(taxInfo.getDateOfBirth());
		taxInfoDto.setTermsAndConditions(taxInfo.getTermsAndConditions());
		taxInfoDto.setCreatedDate(taxInfo.getCreatedDate());
		taxInfoDto.setEditedDate(taxInfo.getEditedDate());
		taxInfoDto.setTaxId(taxInfo.getTaxId());
		return taxInfoDto;
	}
	
	
}