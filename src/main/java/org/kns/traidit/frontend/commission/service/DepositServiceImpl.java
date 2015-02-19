package org.kns.traidit.frontend.commission.service;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.dao.DepositDao;
import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.DepositNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.model.DirectDepositInformation;
import org.kns.traidit.backend.commission.model.TaxInformation;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.commission.dto.DirectDepositInformationDto;
import org.kns.traidit.frontend.commission.dto.TaxInformationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Bhagya -Kns Technologies
 * Service implementation for Deposit and tax
 * Created By Bhagya on JAn 30th,2015
 *
 */
@Transactional
@Service("depositService")
public class DepositServiceImpl implements DepositService{
	
	private static Logger log=Logger.getLogger(DepositServiceImpl.class);
	
	@Resource(name="depositDao")
	private DepositDao depositDao;
	
	@Resource(name = "userDao")
	private UserDao userDao;
	/**
	 * Created By bhagya On Jan 30th,2015
	 * @param userId
	 * @param bankName
	 * @param accountType
	 * @param routingNumber
	 * @param accountNumber
	 * @return
	 * @throws UserNotFoundException
	 * @throws DepositNotSavedOrUpdatedException
	 * 
	 * Method for saving and updating the deposit information
	 * @throws DepositNotFoundException 
	 */
	public Integer saveOrUpdateDepositInformation(Integer depositId,Integer userId,String bankName,String accountType,String routingNumber,String accountNumber) 
			throws UserNotFoundException, DepositNotSavedOrUpdatedException, DepositNotFoundException{
		log.info("inside saveDepositInformation");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		DirectDepositInformation deposit;
		
		if(depositId==0){
			deposit=new DirectDepositInformation();
			deposit.setCreatedDate(new Date());
		}
		else{
		 deposit=this.depositDao.getDepositById(depositId);
		 deposit.setEditedDate(new Date());
		}
		deposit.setTraiditUser(user);
		deposit.setBankName(bankName);
		deposit.setAccountType(accountType);
		deposit.setRoutingNumber(routingNumber);
		deposit.setAccountNumber(accountNumber);
		
		Integer savedResult=this.depositDao.saveOrUpdateDeposit(deposit);
		return savedResult;
		
	}
	
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param taxId
	 * @param userId
	 * @param socialSecurityNo
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param dateOfBirth
	 * @param termsAndConditions
	 * @return
	 * @throws UserNotFoundException
	 * @throws TaxNotFoundException
	 * @throws TaxNotSavedOrUpdatedException
	 * 
	 * Method For Saving and updating the Tax Information
	 */
	
	public Integer saveOrUpdateTaxInformation(Integer taxId,Integer userId,String socialSecurityNo,String firstName,String middleName,String lastName,Date dateOfBirth,
			Boolean termsAndConditions) throws UserNotFoundException, TaxNotFoundException, TaxNotSavedOrUpdatedException{
		log.info("inside saveOrUpdateTaxInformation()");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		TaxInformation taxInfo;
		
		if(taxId==0){
			taxInfo=new TaxInformation();
			taxInfo.setCreatedDate(new Date());
		}
		else{
			taxInfo=this.depositDao.getTaxById(taxId);
			taxInfo.setEditedDate(new Date());
		}
		taxInfo.setTraiditUser(user);
		taxInfo.setSocialSecurityNumber(socialSecurityNo);
		taxInfo.setFirstName(firstName);
		taxInfo.setMiddleName(middleName);
		taxInfo.setLastName(lastName);
		taxInfo.setDateOfBirth(dateOfBirth);
		taxInfo.setTermsAndConditions(termsAndConditions);
		Integer savedResult=this.depositDao.saveOrUpdateTaxInformation(taxInfo);
		return savedResult;
	}
	
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param searchText
	 * @return
	 * @throws DepositNotFoundException
	 * 
	 * Web Application Method For getting the list of all Deposits
	 */
	public ArrayList<DirectDepositInformationDto> getAllDepositsFromDb(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws DepositNotFoundException{
		log.info("inside getAllDepositsFromDb()");
		ArrayList<DirectDepositInformationDto> depositsDtos = new ArrayList<DirectDepositInformationDto>();
		ArrayList<DirectDepositInformation> deposits=this.depositDao.getAllDeposits(pageNo, pageSize, sortBy, sortOrder, searchText);
		for(DirectDepositInformation deposit:deposits){
			DirectDepositInformationDto depositDto=DirectDepositInformationDto.populateDirectDepositInformationDto(deposit);
			depositsDtos.add(depositDto);
		}
		return depositsDtos;
	}
	/**
	 * Created By Bhagya on Feb 03rd,2015
	 * @return
	 * @throws Exception
	 * 
	 * Web Application Method For getting total results of deposits
	 */
	public Integer getTotalResultsOfDeposits() throws DepositNotFoundException{
		log.info("inside getTotalResultsOfDeposits()");
		Integer totalResults=this.depositDao.getTotalCountOfDeposits();
		return totalResults;
		
	}
	/**
	 * Created By Bhagya on feb 03rd,2015
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param searchText
	 * @return
	 * @throws TaxNotFoundException
	 * 
	 * Web Application Method for getting all users taxes Information
	 */
	public ArrayList<TaxInformationDto> getAllTaxesFromDb(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws TaxNotFoundException{
		log.info("inside getAllTaxesFromDb()");
		ArrayList<TaxInformationDto> taxesDtos = new ArrayList<TaxInformationDto>();
		ArrayList<TaxInformation> taxes=this.depositDao.getAllTaxes(pageNo, pageSize, sortBy, sortOrder, searchText);
		for(TaxInformation tax:taxes){
			TaxInformationDto taxDto=TaxInformationDto.popTaxInformationDto(tax);
			taxesDtos.add(taxDto);
		}
		return taxesDtos;
	}
	/**
	 * Created By Bhagya on Feb 03rd,2015
	 * @return
	 * @throws Exception
	 * 
	 * Web Application Method For getting total results of deposits
	 */
	public Integer getTotalResultsOfTaxes() throws Exception{
		log.info("inside getTotalResultsOfTaxes()");
		Integer totalResults=this.depositDao.getTotalCountOfTaxes();
		return totalResults;
		
	}
	
	/**
	 * Created By Bhagya on Feb 04th,2015
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 * @throws DepositNotFoundException
	 * 
	 * Mobile Service,For getting the deposit information By userId
	 */
	public DirectDepositInformationDto getDepositByUserId(Integer userId) throws UserNotFoundException, DepositNotFoundException{
		log.info("inside getDepositByUserId()");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		DirectDepositInformation deposit=this.depositDao.getDepositByUserFromDb(user);
		DirectDepositInformationDto depositDto=DirectDepositInformationDto.populateDirectDepositInformationDto(deposit);
		return depositDto;
	}
	
	/**
	 * Created By Bhagya On Feb 04th,2015
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 * @throws TaxNotFoundException
	 * 
	 * Mobile Service,For getting the tax information By userId
	 */
	public TaxInformationDto getTaxDetailsByUserId(Integer userId) throws UserNotFoundException, TaxNotFoundException{
		log.info("inside getTaxDetailsByUserId()");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		TaxInformation taxInfo=this.depositDao.getTaxDetailsByUserFromDb(user);
		TaxInformationDto taxInfoDto=TaxInformationDto.popTaxInformationDto(taxInfo);
		return taxInfoDto;
		
		
	}
	}
