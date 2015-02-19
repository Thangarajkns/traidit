package org.kns.traidit.frontend.commission.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoryListFormBean;
import org.kns.traidit.frontend.commission.dto.DirectDepositInformationDto;
import org.kns.traidit.frontend.commission.dto.TaxInformationDto;
import org.kns.traidit.frontend.commission.service.DepositService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created By Bhagya on Feb 03rd,2015
 * @author Praga
 *	Controller for web services of deposit and tax Information
 */
@Controller("webDepositController")
@RequestMapping(value="/web")
public class DepositController{
	private Logger log = Logger.getLogger(DepositController.class);
	
	
	@Resource(name="depositService")
	private DepositService depositService;
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param categoryListFormBean
	 * @param map
	 * @return
	 * @throws Exception
	 * 
	 * Web Application Method for displaying list of all deposits
	 */
	@RequestMapping(value="depositslist.htm")
	public String viewAllDeposits(@ModelAttribute("categoryListFormBean")CategoryListFormBean categoryListFormBean,Map<String,Object> map) throws Exception{
		log.info("inside viewAllDeposits()");
		ArrayList<DirectDepositInformationDto> deposits = null;
		Integer totalDeposits = 0;
		
		try{
			
			 deposits=this.depositService.getAllDepositsFromDb(
					categoryListFormBean.getPaginator().getCurrentPageNo(),
					categoryListFormBean.getPaginator().getNoOfItemsPerPage(),
					categoryListFormBean.getSortBy(),
					categoryListFormBean.getSortOrder(),
					categoryListFormBean.getSearchText()
					);
			
		}
		catch(DepositNotFoundException e){
			 System.out.println("No Deposits found ");
		}
		
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error Occured on while viewing deposits");
		}
		finally{
		if(deposits!=null && !deposits.isEmpty())
			totalDeposits = this.depositService.getTotalResultsOfDeposits();
		    categoryListFormBean.getPaginator().setTotalNoOfItems(totalDeposits);
			
		}
		Integer i=categoryListFormBean.getPaginator().getStarttIndex();
		map.put("deposits", deposits);
		map.put("i", i+1);
		return "listDeposits";
	}
	
	/**
	 * Created By Bhagya On Feb 03rd,2015
	 * @param categoryListFormBean
	 * @param map
	 * @return
	 * @throws Exception
	 * 
	 * Web Application Method for displaying list of all taxes
	 */
	@RequestMapping(value="taxeslist.htm")
	public String viewAllTaxes(@ModelAttribute("categoryListFormBean")CategoryListFormBean categoryListFormBean,Map<String,Object> map) throws Exception{
		log.info("inside viewAllTaxes()");
		ArrayList<TaxInformationDto> taxes = null;
		Integer totalTaxes = 0;
		try{
			 taxes=this.depositService.getAllTaxesFromDb(
					categoryListFormBean.getPaginator().getCurrentPageNo(),
					categoryListFormBean.getPaginator().getNoOfItemsPerPage(),
					categoryListFormBean.getSortBy(),
					categoryListFormBean.getSortOrder(),
					categoryListFormBean.getSearchText()
					);
			
		}
		catch(TaxNotFoundException e){
			 System.out.println("No Taxes found ");
			 
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error Occured on while viewing taxes");
		}
		finally{
			if(null!=taxes && !taxes.isEmpty())
				totalTaxes = this.depositService.getTotalResultsOfDeposits();
				categoryListFormBean.getPaginator().setTotalNoOfItems(totalTaxes);
		}
		Integer i=categoryListFormBean.getPaginator().getStarttIndex();
		map.put("taxes", taxes);
		map.put("i", i+1);
		return "listTaxes";
	}
	
}