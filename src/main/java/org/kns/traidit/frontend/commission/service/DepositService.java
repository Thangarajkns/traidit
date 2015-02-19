package org.kns.traidit.frontend.commission.service;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.DepositNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.DirectDepositInformationDto;
import org.kns.traidit.frontend.commission.dto.TaxInformationDto;

/**
 * 
 * @author Bhagya -Kns Technologies
 * Service for Deposit and tax
 * Created By Bhagya on JAn 30th,2015
 *
 */
public interface DepositService{
	public Integer saveOrUpdateDepositInformation(Integer depositId,Integer userId,String bankName,String accountType,String routingNumber,String accountNumber) 
			throws UserNotFoundException, DepositNotSavedOrUpdatedException, DepositNotFoundException;
	public Integer saveOrUpdateTaxInformation(Integer taxId,Integer userId,String socialSecurityNo,String firstName,String middleName,String lastName,Date dateOfBirth,
			Boolean termsAndConditions) throws UserNotFoundException, TaxNotFoundException, TaxNotSavedOrUpdatedException;
	public ArrayList<DirectDepositInformationDto> getAllDepositsFromDb(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws DepositNotFoundException;
	public Integer getTotalResultsOfDeposits() throws DepositNotFoundException;
	public ArrayList<TaxInformationDto> getAllTaxesFromDb(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws TaxNotFoundException;
	public Integer getTotalResultsOfTaxes() throws Exception;
	public DirectDepositInformationDto getDepositByUserId(Integer userId) throws UserNotFoundException, DepositNotFoundException;
	public TaxInformationDto getTaxDetailsByUserId(Integer userId) throws UserNotFoundException, TaxNotFoundException;
	
}