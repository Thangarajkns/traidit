package org.kns.traidit.backend.commission.dao;

import java.util.ArrayList;




import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.request.CoreAdminRequest.Create;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.DepositNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.model.DirectDepositInformation;
import org.kns.traidit.backend.commission.model.TaxInformation;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Bhagya -Kns Technologies
 *Dao Implementation for Deposit and tax
 * Created By Bhagya on JAn 30th,2015
 *
 */
@Repository("depositDao")
@Transactional
public class DepositDaoImpl implements DepositDao{
	private static Logger log=Logger.getLogger(DepositDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	/***
	 * Created By Bhagya On Jan 30th,2015
	 * @param deposit
	 * @return
	 * @throws DepositNotSavedOrUpdatedException
	 * 
	 * Method For Saving or Updating the direct deposit Information
	 */
	public Integer saveOrUpdateDeposit(DirectDepositInformation deposit)throws DepositNotSavedOrUpdatedException{
		log.info("inside saveorUpdateDeposit()");
		try{
			 sessionFactory.getCurrentSession().saveOrUpdate(deposit);
			 sessionFactory.getCurrentSession().flush();
		}
		catch(Exception e){
			log.error(e.toString());
			throw new DepositNotSavedOrUpdatedException();
		}
		 return deposit.getDepositId();
	 }
	/**
	 * Created By Bhagya On Jan 30th,2015
	 * @param depositId
	 * @return
	 * @throws DepositNotFoundException
	 * 
	 * Method For getting the Deposit By Id
	 */
	
	@SuppressWarnings("unchecked")
	public DirectDepositInformation getDepositById(Integer depositId) throws DepositNotFoundException {
		log.info("inside getDepositById()");
		ArrayList<DirectDepositInformation> deposit = (ArrayList<DirectDepositInformation>) sessionFactory.getCurrentSession().createCriteria(DirectDepositInformation.class)
				.add(Restrictions.eq("depositId", depositId))
				.list();
		
		if(!deposit.isEmpty()){
			return deposit.get(0);
		}
		else{
			throw new DepositNotFoundException();
		}	
	}
	
	/***
	 * Created By Bhagya On Feb 03rd,2015
	 * @param tax
	 * @return
	 * @throws TaxNotSavedOrUpdatedException
	 * 
	 * Method For Saving or Updating the direct tax Information
	 */
	public Integer saveOrUpdateTaxInformation(TaxInformation tax)throws TaxNotSavedOrUpdatedException{
		log.info("inside saveOrUpdateTaxInformation()");
		try{
		sessionFactory.getCurrentSession().saveOrUpdate(tax);
		sessionFactory.getCurrentSession().flush();
		}
		catch(Exception e){
			log.error(e.toString());
			throw new TaxNotSavedOrUpdatedException();
		}
		return tax.getTaxId();
	}
	
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param taxId
	 * @return
	 * @throws TaxNotFoundException
	 * 
	 * Method For getting the Tax By Id
	 */
	
	@SuppressWarnings("unchecked")
	public TaxInformation getTaxById(Integer taxId) throws TaxNotFoundException {
		log.info("inside getTaxById()");
		ArrayList<TaxInformation> tax = (ArrayList<TaxInformation>) sessionFactory.getCurrentSession().createCriteria(TaxInformation.class)
				.add(Restrictions.eq("taxId", taxId))
				.list();
		
		if(!tax.isEmpty()){
			return tax.get(0);
		}
		else{
			throw new TaxNotFoundException();
		}	
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
	 *Web application  Method for getting the list of all deposits from db
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DirectDepositInformation> getAllDeposits(Integer pageNo,Integer pageSize,String sortBy,String sortOrder,String searchText) throws DepositNotFoundException{
		log.info("inside getAllDeposits()");
		ArrayList<DirectDepositInformation> deposits=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(DirectDepositInformation.class).createAlias("traiditUser", "user");

		 if(searchText != null && !searchText.isEmpty()){
			 
			 Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("user.firstName", searchText, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("user.lastName", searchText, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("user.middleName", searchText, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		 }
		 if(sortBy == null || sortBy.isEmpty())
			 sortBy = "bankName";
		 if(sortOrder != null && sortOrder.equals("DESC"))
			 criteria.addOrder(Order.desc(sortBy));
		 else
			 criteria.addOrder(Order.asc(sortBy));
		 
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		deposits=(ArrayList<DirectDepositInformation>) criteria.list();
		 if(!deposits.isEmpty()){
			 return deposits;
			 
		 }
		 else{
			 throw new DepositNotFoundException();
		 }
	}
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @return
	 * @throws Exception
	 * 
	 * Web application Method for getting the total count of deposits
	 */
	
	public Integer getTotalCountOfDeposits() throws DepositNotFoundException{
		log.info("inside getTotalCountOfDeposits()");
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(DirectDepositInformation.class);
		criteria.setProjection(Projections.count("depositId")).list();
		Integer totalResults=Integer.valueOf (criteria.list().get(0).toString());
		return totalResults;
	}
	
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param searchText
	 * @return
	 * @throws TaxNotFoundException
	 * 
	 *Web application  Method for getting the list of all taxes from db
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TaxInformation> getAllTaxes(Integer pageNo,Integer pageSize,String sortBy,
			String sortOrder,String searchText) throws TaxNotFoundException{
		log.info("inside getAllTaxes()");
		ArrayList<TaxInformation> taxes=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TaxInformation.class);
		 if(searchText != null && !searchText.isEmpty()){
			 Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("firstName", searchText, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("lastName", searchText, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("middleName", searchText, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		 }
		
		 if(sortBy == null || sortBy.isEmpty())
			 sortBy = "firstName";
		 if(sortOrder != null && sortOrder.equals("DESC"))
			 criteria.addOrder(Order.desc(sortBy));
		 else
			 criteria.addOrder(Order.asc(sortBy));
		 
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		taxes=(ArrayList<TaxInformation>) criteria.list();
		 if(!taxes.isEmpty()){
			 return taxes;
			 
		 }
		 else{
			 throw new TaxNotFoundException();
		 }
	}
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @return
	 * @throws Exception
	 * 
	 * Web application Method for getting the total count of taxes
	 */
	
	public Integer getTotalCountOfTaxes() throws Exception{
		log.info("inside getTotalCountOfDeposits()");
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(DirectDepositInformation.class);
		criteria.setProjection(Projections.count("taxId")).list();
		Integer totalResults=Integer.valueOf (criteria.list().get(0).toString());
		return totalResults;
	}
	
	/**
	 * Created By Bhagya on Feb 04th,2015
	 * @param user
	 * @return
	 * @throws DepositNotFoundException
	 * 
	 * For Mobile dao method,getting the deposit by tridituser
	 */
	@SuppressWarnings("unchecked")
	public DirectDepositInformation getDepositByUserFromDb(TraidItUser user) throws DepositNotFoundException{
		log.info("inside getDepositByUserFromDb()");
		
		ArrayList<DirectDepositInformation> deposit = (ArrayList<DirectDepositInformation>) sessionFactory.getCurrentSession().createCriteria(DirectDepositInformation.class)
				.add(Restrictions.eq("traiditUser", user))
				.list();
		
		if(!deposit.isEmpty()){
			return deposit.get(0);
		}
		else{
			throw new DepositNotFoundException();
		}	
		
	}
	
	/**
	 * Created By Bhagya on Feb 04th,2015
	 * @param user
	 * @return
	 * @throws TaxNotFoundException
	 * 
	 * For Mobile dao method,getting the Tax details by tridituser
	 */
	@SuppressWarnings("unchecked")
	public TaxInformation getTaxDetailsByUserFromDb(TraidItUser user) throws TaxNotFoundException{
		log.info("inside getTaxDetailsByUserFromDb()");
		ArrayList<TaxInformation> tax = (ArrayList<TaxInformation>) sessionFactory.getCurrentSession().createCriteria(TaxInformation.class)
				.add(Restrictions.eq("traiditUser", user))
				.list();
		if(!tax.isEmpty()){
			return tax.get(0);
		}
		else{
			throw new TaxNotFoundException();
		}	
	}
}