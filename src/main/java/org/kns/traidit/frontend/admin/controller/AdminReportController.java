/**
 * @since 18-Feb-2015
 */
package org.kns.traidit.frontend.admin.controller;

import java.text.ParseException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.persistence.QueryHint;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.user.exception.CommissionPaymentsNotFoundException;
import org.kns.traidit.backend.user.exception.CommissionsNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.CommissionDto;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Controller("adminReportController")
public class AdminReportController {
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="commissionService")
	private CommissionService commissionService;

	private static Logger log=Logger.getLogger(JasperReportController.class);

	/**
	 * 
	 * @param model
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 17-Feb-2015
	 */
	@RequestMapping(value="/web/reports/listthismonthcommission.htm")
	public String getCommissionsToBePaidThisMonth(Model model){
		ArrayList<CommissionDto> commissionList = null;
		try {
			commissionList = this.commissionService.getMonthlyCommissionsOfAllUsers();
		} catch (CommissionsNotFoundException e) {
			log.error("CommissionsNotFoundException : no commission found");
		} catch (UserNotFoundException e) {
			log.error("UserNotFoundException : no commission found");
		} 
		model.addAttribute("commissions",commissionList);
		return "reports/listCommissions";
	}
	
	/**
	 * 
	 * @param model
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 17-Feb-2015
	 */
	@RequestMapping(value="/web/reports/commissionsyeartodate.htm")
	public String getCommissionsYearToDate(Model model){
		ArrayList<CommissionDto> commissionList = null;
		try {
			commissionList = this.commissionService.getCommissionsFromStartOfYearToToday();
		} catch (CommissionsNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("commissions",commissionList);
		return "reports/listCommissions";
	}

	@RequestMapping(value="/web/reports/listpayment.htm")
	public String getPaymentsReport(@RequestParam(value="option",required=false,defaultValue="week")String option,Model model){
		ArrayList<UserDto> users = new ArrayList<UserDto>();
		try {
			users = this.userService.getUsersBasedOnTodayPaymentsDues();
		} catch (UserNotFoundException e) {
			log.error("UserNotFoundException : no users found");
		}
		model.addAttribute("paymentDues",users);
		return "reports/listpayment";
	}
	
}
