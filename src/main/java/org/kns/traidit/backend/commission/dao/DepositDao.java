package org.kns.traidit.backend.commission.dao;

import java.util.ArrayList;

import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.DepositNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.model.DirectDepositInformation;
import org.kns.traidit.backend.commission.model.TaxInformation;
import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * 
 * @author Bhagya -Kns Technologies
 * Dao Interface  for Deposit and tax
 * Created By Bhagya on JAn 30th,2015
 *
 */
public interface DepositDao{
	public Integer saveOrUpdateDeposit(DirectDepositInformation deposit)throws DepositNotSavedOrUpdatedException;
	public DirectDepositInformation getDepositById(Integer depositId) throws DepositNotFoundException ;
	public Integer saveOrUpdateTaxInformation(TaxInformation tax)throws TaxNotSavedOrUpdatedException;
	public TaxInformation getTaxById(Integer taxId) throws TaxNotFoundException;
	public ArrayList<DirectDepositInformation> getAllDeposits(Integer pageNo,Integer pageSize,String sortBy,String sortOrder,String searchText) throws DepositNotFoundException;
	public Integer getTotalCountOfDeposits() throws DepositNotFoundException;
	public ArrayList<TaxInformation> getAllTaxes(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws TaxNotFoundException;
	public Integer getTotalCountOfTaxes() throws Exception;
	public DirectDepositInformation getDepositByUserFromDb(TraidItUser user) throws DepositNotFoundException;
	public TaxInformation getTaxDetailsByUserFromDb(TraidItUser user) throws TaxNotFoundException;
}