/**
 * @since 17-Dec-2014
 */
package org.kns.traidit.frontend.commission.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.kns.traidit.frontend.category.controller.CategoryController;
import org.kns.traidit.frontend.commission.dto.CommissionPaymentDto;
import org.kns.traidit.frontend.commission.dto.CommissionPaymentsFormBean;
import org.kns.traidit.frontend.commission.dto.UserCommissionsFormBean;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.commission.service.CommissionServiceImpl;
import org.kns.traidit.frontend.payments.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@Controller("webCommissionController")
@RequestMapping(value="/web/commission")
public class CommissionController {
	
	private Logger log = Logger.getLogger(CategoryController.class);
	
	@Resource(name="commissionService")
	private CommissionService commissionService;

	@Resource(name="paymentService")
	private PaymentService paymentService;
	
	@RequestMapping(value="home.htm")
	public String commissionsHomePage(Map<String,Object> map,@ModelAttribute("userCommissionsForm")UserCommissionsFormBean userCommissionForm){
		Date fromDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(fromDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		fromDate = cal.getTime();
		cal.add(Calendar.MONTH,+1);
		Date toDate = cal.getTime();
		
		BigDecimal totalRevenue = this.paymentService.getSubscriptionAmountByDate(fromDate, toDate);
		BigDecimal commissionSpent =  this.commissionService.getCommissionByDate(fromDate, toDate);
		BigDecimal actualRevenue =  totalRevenue.subtract(commissionSpent);
		
		userCommissionForm.setCommissions(this.commissionService.getCommissionsOfAllUsers(
				userCommissionForm.getSortBy(),
				userCommissionForm.getSortOrder(),
				userCommissionForm.getPaginator().getNoOfItemsPerPage(), 
				userCommissionForm.getPaginator().getStarttIndex()));
		userCommissionForm.getPaginator().setTotalNoOfItems(CommissionServiceImpl.totalNoOfResults);
		userCommissionForm.setTotalRevenue(totalRevenue);
		userCommissionForm.setCommissionSpent(commissionSpent);
		userCommissionForm.setActualRevenue(actualRevenue);
		
		map.put("userCommissionsForm", userCommissionForm);
		return "commissionHome";
	}
	
	@RequestMapping(value="payments.htm")
	public String CommissionPayments(HttpServletRequest request,Map<String,Object> map,@ModelAttribute("commissionPaymentsForm")CommissionPaymentsFormBean commissionPaymentsFormBean){
		 ArrayList<CommissionPaymentDto> payments = this.commissionService.getPaymentsOfUser(
					commissionPaymentsFormBean.getUser().getUserId(),
					commissionPaymentsFormBean.getSortBy(),
					commissionPaymentsFormBean.getSortOrder(),
					commissionPaymentsFormBean.getPaginator().getNoOfItemsPerPage(), 
					commissionPaymentsFormBean.getPaginator().getStarttIndex()
					);
		map.put("commissionPayments",payments);
		commissionPaymentsFormBean.getPaginator().setTotalNoOfItems(CommissionServiceImpl.totalNoOfResults);
		commissionPaymentsFormBean.setReferer(request.getHeader("referer"));
		map.put("commissionPaymentsForm", commissionPaymentsFormBean);
		return "commissionPayments";
	}

}
