package org.kns.traidit.frontend.mobile.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.exception.DepositNotFoundException;
import org.kns.traidit.backend.commission.exception.DepositNotSavedOrUpdatedException;
import org.kns.traidit.backend.commission.exception.TaxNotFoundException;
import org.kns.traidit.backend.commission.exception.TaxNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.DirectDepositInformationDto;
import org.kns.traidit.frontend.commission.dto.TaxInformationDto;
import org.kns.traidit.frontend.commission.service.DepositService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONObject;

/**
 * 
 * @author Bhagya -Kns Technologies
 * Controller for Deposit and tax
 * Created By Bhagya on JAn 30th,2015
 *
 */
@Controller("DepositController")
public class DepositController{
	private static Logger log=Logger.getLogger(DepositController.class);
	

	@Resource(name="depositService")
	private DepositService depositService;
	
	
	/**
	 * Created By Bhagya On Jan 30th,2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * Method for saving and updating the deposit Information
	 */
	
	@RequestMapping("/savedeposit.htm")
	@ResponseBody
	public String saveOrUpdateDepositInformation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside saveDeposit()");
		System.out.println("inside controller");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			JSONObject json = JsonParser.getJson(request, response);
			System.out.println("request : "+json.toString());
			log.info("request : "+json.toString());
			 
			Integer depositId=json.getInt("depositid");
			Integer userId=json.getInt("userid");
			String bankName=json.getString("bankname");
			String accountType=json.getString("accounttype");
			String routingNumber=json.getString("routingnumber");
			String accountNumber=json.getString("accountnumber");
			Integer savedResult=this.depositService.saveOrUpdateDepositInformation(depositId,userId, bankName, accountType, routingNumber, accountNumber);
			if(savedResult>0){
				String status="Deposit Saved/Edited";
				obj.accumulate("status", status);
				obj.accumulate("id", savedResult);
			}
			
		}
		catch(UserNotFoundException e){
			error=e.toString();
			log.error(error);
			String message="User Not Found";
			obj.accumulate("status", message);
			
		}
		catch(DepositNotFoundException e){
			error=e.toString();
			log.error(error);
			String message="Deposit Not Found";
			obj.accumulate("status", message);
			
		}
		catch(DepositNotSavedOrUpdatedException e){
			error=e.toString();
			log.error(error);
			String message="Error In Saving Or Updating Deposit";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.info(e.toString());
			obj.accumulate("status", "Some Error On Deposit data");
			
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
		
	}
	/**
	 * Created By Bhagya on feb 03rd,2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * Method for saving and updating the tex information
	 */
	@RequestMapping("/savetax.htm")
	@ResponseBody
	public String saveOrUpdateTaxInformation(HttpServletRequest request,HttpServletResponse response)throws Exception{
		log.info("inside saveOrUpdateTaxInformation()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			JSONObject json = JsonParser.getJson(request, response);
			System.out.println("request : "+json.toString());
			log.info("request : "+json.toString());
			
			Integer taxId=json.getInt("taxid");
			Integer userId=json.getInt("userid");
			String socialSecurityNo=json.getString("socialsecuritynumber");
			String firstName=json.getString("firstname");
			String middleName=json.getString("middlename");
			String lastName=json.getString("lastname");
			Boolean termsAndConditions=true;
			String birthDate=json.getString("dateofbirth");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date dateOfBirth=simpleDateFormat.parse(birthDate);
			
			Integer savedResult=this.depositService.saveOrUpdateTaxInformation(taxId, userId, socialSecurityNo, firstName, middleName, lastName, dateOfBirth, termsAndConditions);
			if(savedResult>0){
				String status="Tax Saved/Edited";
				obj.accumulate("status", status);
				obj.accumulate("id", savedResult);
			}
			
		}
		catch(UserNotFoundException e){
			error=e.toString();
			log.error(error);
			String message="User Not Found";
			obj.accumulate("status", message);
		}
		catch(TaxNotFoundException e){
			error=e.toString();
			log.error(error);
			String message="Tax Not Found";
			obj.accumulate("status", message);
		}
		catch(TaxNotSavedOrUpdatedException e){
			error=e.toString();
			log.error(error);
			String message="Error In Saving Or Updating Tax";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.info(e.toString());
			obj.accumulate("status", "Some error On Tax data");
			
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	/**
	 * Created By Bhagya on feb 04th,2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 *Controller Method For Mobile service
	 * Displaying the deposit information based on requested user
	 */
	@RequestMapping("/displaydepositdetails.htm")
	@ResponseBody
	public String displayDepositInformation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("displayDepositInformation");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			 
			 Integer userId=json.getInt("userid");
			 DirectDepositInformationDto deposit=this.depositService.getDepositByUserId(userId);
			 obj.accumulate("username", deposit.getTraiditUser().getUserName());
			 obj.accumulate("bankname", deposit.getBankName());
			 obj.accumulate("accounttype", deposit.getAccountType());
			 obj.accumulate("routingnumber", deposit.getRoutingNumber());
			 obj.accumulate("accountnumber", deposit.getAccountNumber());
			 obj.accumulate("id", deposit.getDepositId());
		}
		catch(UserNotFoundException e){
			log.error("User Not Found "+e.toString());
			String message="User Not Found";
			obj.accumulate("status", message);
		}
		catch(DepositNotFoundException e){
			log.error("Deposit Not Found "+e.toString());
			String message="Deposit Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.error("Error in getting Deposit Details "+e.toString());
			String message="Error Getting Deposit Details";
			e.printStackTrace();
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
		
	}
	/**
	 * Created By Bhagya On feb 04th,2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * Controller  Method For Mobile service
	 * Displaying the tax information based on requested user
	 */
	@RequestMapping("/displaytaxdetails.htm")
	@ResponseBody
	public String displayTaxInformation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("displayTaxInformation");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			 
			Integer userId=json.getInt("userid");
			TaxInformationDto tax=this.depositService.getTaxDetailsByUserId(userId);
			obj.accumulate("socialsecuritynumber", tax.getSocialSecurityNumber());
			obj.accumulate("firstname", tax.getFirstName());
			obj.accumulate("middlename", tax.getMiddleName());
			obj.accumulate("lastname", tax.getLastName());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			obj.accumulate("dateofbirth", simpleDateFormat.format(tax.getDateOfBirth()));
			obj.accumulate("id", tax.getTaxId());
		}
		catch(UserNotFoundException e){
			log.error("User Not Found "+e.toString());
			String message="User Not Found";
			obj.accumulate("status", message);
		}
		catch(TaxNotFoundException e){
			log.error("Tax Not Found "+e.toString());
			String message="Tax Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.error("Error in getting Tax Details "+e.toString());
			String message="Error Getting Tax Details";
			e.printStackTrace();
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
		
	}
}