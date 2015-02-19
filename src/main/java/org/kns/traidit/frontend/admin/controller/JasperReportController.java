/**
 * @since 04-Feb-2015
 */
package org.kns.traidit.frontend.admin.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.user.exception.CommissionPaymentsNotFoundException;
import org.kns.traidit.backend.user.exception.CommissionsNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.CommissionDto;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@Controller("jasperReportController")
public class JasperReportController {
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="commissionService")
	private CommissionService commissionService;

	private static Logger log=Logger.getLogger(JasperReportController.class);
	
	/**
	 * Created by bhagya on Feb 05th,2015
	 * get Method for the example of jasper report 
	 */
	
	@RequestMapping(method=RequestMethod.GET,value="/reports/getfirstreport.htm")
	public String firstReport(){
		log.info("inside firstReport()");
		try{
			return "report";
		}
		catch(Exception e){
			System.out.println("inside error condition of jasper controller at get method");
			return "error";
		}
		
		
	}
	/**
	 * Created by bhagya on Feb 05th,2015
	 * Post Method for the example of jasper report 
	 */
	
	@RequestMapping(method=RequestMethod.POST,value="/reports/getfirstreport.htm")
	public String getFirstReport(Map<String, Object> map){
		log.info("inside getFirstReport of post method()");
		try{
			ArrayList<UserDto> users = this.userService.viewUsers(null, 0, 0, null, "asc");
			JRDataSource jrDataSource=new JRBeanCollectionDataSource(users);
			map.put("datasource", jrDataSource);
			return "exampleReport";
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("inside error condition of jasper controller at post method");
			return "error";
		}


	}
	/**
	 * Created By Bhagya On Feb 16th,2015
	 * Get Method For Payments Reports
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/reports/getpayments.htm")
	public String paymentsDueTodayReport(){
		log.info("inside paymentsDueTodayReport()");
		try{
			return "reports/paymentReports";
		}
		catch(Exception e){
			System.out.println("inside error condition of jasper controller at get method of today payments");
			return "error";
		}
		
	}
	
	/**
	 * Created By Bhagya On Feb 16th,2015
	 * @param map
	 * @return
	 * 
	 * Method for Viewing the Payments Dues of User for  Toady
	 */
	@RequestMapping(method=RequestMethod.GET,value="/reports/gettodaypayments.htm")
	public String getTodayPaymentsReport(Map<String, Object> map,@RequestParam(value="export",required=false)String export){
		log.info("inside getTodayPaymentsReport()");
		try{
			
			ArrayList<UserDto> users = this.userService.getUsersBasedOnTodayPaymentsDues();
			if(null!=export && export.equalsIgnoreCase("excel")){
				Map<String, UserDto> paymentsData=new LinkedHashMap<String, UserDto>();
				int i=0;
				for(UserDto user:users){
					paymentsData.put("user"+i,user);
					i++;
				}
				map.put("usersPayments", paymentsData);				
				return "paymentsXlsReport";
				
			}
			else{
				JRDataSource jrDataSource=new JRBeanCollectionDataSource(users);
				map.put("datasource", jrDataSource);
				return "paymentsReport";
			}
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("inside error condition of jasper controller at payment due today post method");
			return "error";
		}
	}
	/**
	 * Created By Bhagya On Feb 16th,2015
	 * @param term,map
	 * @return
	 * Method For getting the Weekly Payments Of users
	 */
	
	@RequestMapping(method=RequestMethod.GET,value="/reports/getweeklypayments.htm")
	public String getWeeklyPaymentReports(@RequestParam("term") String term,Map<String, Object> map,@RequestParam(value="export",required=false)String export){
		log.info("inside getWeeklyPaymentReports()");
		
		try{
			ArrayList<UserDto> users = this.userService.getPaymentReportsByTerm(term);
			if(null!=export && export.equalsIgnoreCase("excel")){
				Map<String, UserDto> paymentsData=new LinkedHashMap<String, UserDto>();
				int i=0;
				for(UserDto user:users){
					paymentsData.put("user"+i,user);
					i++;
				}
				map.put("usersPayments", paymentsData);				
				return "paymentsXlsReport";
			}
			else{
				JRDataSource jrDataSource=new JRBeanCollectionDataSource(users);
				map.put("datasource", jrDataSource);
				return "paymentsReport";
			}		
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("inside error condition of jasper controller at payment due today post method");
			return "error";
		}
	}
	
	
	/**
	 * Created By Bhagya On Feb 18th,2015
	 * @param map
	 * @return
	 * Method for getting the actual monthly commissions list.....
	 * That Commissions need to pay By admin..
	 */
	
	@RequestMapping(method=RequestMethod.GET,value="/reports/getmonthlycommissions.htm")
	public String getMonthlyCommissions(Map<String,Object> map,@RequestParam(value="export",required=false)String export){
		log.info("inside getMonthlyCommissions()");
		
		try{
			ArrayList<CommissionDto> commissions=this.commissionService.getMonthlyCommissionsOfAllUsers();
			if(null!=export && export.equalsIgnoreCase("excel")){
				Map<String,CommissionDto> commissionsData=new LinkedHashMap<String, CommissionDto>();
				int i=0;
				for(CommissionDto commission:commissions){
					commissionsData.put("commission"+i, commission);
					i++;
				}
				map.put("commissions", commissionsData);
				return "commissionsXlsReport";
			}
			else{
				JRDataSource jrDataSource=new JRBeanCollectionDataSource(commissions);
				map.put("datasource", jrDataSource);
				return "commissionsReport";
			}
		}
		catch(CommissionsNotFoundException e){
			System.out.println("no commissions found with given criteria");
			log.info("no commissions found with given criteria.");
			return "error";
		}
		catch(CommissionPaymentsNotFoundException e){
			System.out.println("no commission payments found with given criteria.");
			log.info("no commission payments found with given criteria.");
			return "error";
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("inside error condition of jasper controller at monthly commission payments");
			return "error";
		}
		
	}
	/**
	 * Craeted By Bhagya On Feb 18th,2015
	 * @param map
	 * @return
	 * Method For getting the commissions from start of year to Today Date
	 */
	@RequestMapping(method=RequestMethod.GET,value="/reports/getcommissionsfromstartofyear.htm")
	public String getCommissionsFromStartOfYearToToday(Map<String,Object> map,@RequestParam(value="export",required=false)String export){
		log.info("inside getCommissionsFromStartOfYearToToday()");
		try{
			ArrayList<CommissionDto> commissions=this.commissionService.getCommissionsFromStartOfYearToToday();
			if(null!=export && export.equalsIgnoreCase("excel")){
				Map<String,CommissionDto> commissionsData=new LinkedHashMap<String, CommissionDto>();
				int i=0;
				for(CommissionDto commission:commissions){
					commissionsData.put("commission"+i, commission);
					i++;
				}
				map.put("commissions", commissionsData);
				return "commissionsXlsReport";
			}
			else{
				JRDataSource jrDataSource=new JRBeanCollectionDataSource(commissions);
				map.put("datasource", jrDataSource);
				return "commissionsReport";
			}
		}
		catch(CommissionsNotFoundException e){
			System.out.println("no commissions found with given criteria");
			log.info("no commissions found with given criteria.");
			return "error";
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("inside error condition of jasper controller at  commission of from start of year");
			return "error";
		}
		
	}
}
