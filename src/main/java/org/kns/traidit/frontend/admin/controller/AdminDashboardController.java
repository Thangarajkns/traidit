/**
 * @since 05-Feb-2015
 */
package org.kns.traidit.frontend.admin.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.kns.traidit.backend.commission.model.TermsAndPrivacyPolicy;
import org.kns.traidit.frontend.admin.dto.PrivacyPolicyFormBean;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.payments.service.PaymentService;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Controller("adminDashboardController")
public class AdminDashboardController {

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "commissionService")
	private CommissionService commissionService;

	@Resource(name = "paymentService")
	private PaymentService paymentService;

	@RequestMapping(value = { "/web/index.htm" }, method = RequestMethod.GET)
	public String onIndexRequest(Map<String, Object> map, HttpServletRequest req) {
		try {
			// TraidItUser user=
			// (TraidItUser)req.getSession().getAttribute("user");
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.AM_PM, Calendar.AM);
			Date currentDate = new Date();
			Date monthStartDate = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			Date yearStartDate = cal.getTime();
			cal.set(Calendar.YEAR, -1);
			Date lastYearStartDate = cal.getTime();
			cal.setTime(monthStartDate);
			cal.set(Calendar.YEAR, -1);
			Date sameMonthLastYearDate = cal.getTime();

			map.put("newSubscribersThisMonth", this.userService.getNewSubscribersCountFrom(monthStartDate));
			map.put("revenueThisMonth", this.commissionService.getRevenueAmountByDate(monthStartDate, currentDate));
			map.put("revenueThisYear", this.commissionService.getRevenueAmountByDate(yearStartDate, currentDate));
			map.put("revenueSameMonthLastYearToDate",
					this.commissionService.getRevenueAmountByDate(sameMonthLastYearDate, currentDate));
			map.put("revenueLastYearToDate",
					this.commissionService.getRevenueAmountByDate(lastYearStartDate, currentDate));
			map.put("tradersCount", this.userService.getTradersCountWithSubscriptionType());
			map.put("subscribersToPayCommission", 1);
			map.put("subscribersPaymentToProcess", 1);
			ArrayList<Object[]> list = this.userService.getUsersCountByDateAndPlan();
			map.put("userscount", list);
			return "adminDash";
		} catch (Exception e) {
			System.out.println("Error while fetching dashboard data" + e.toString());
			return "error";
		}
	}

	/**
	 * 
	 * @param directDepositPrivacyPolicyForm
	 * @param taxFormPrivacyPolicyForm
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value = "/web/privacypolicies.htm")
	public String getPrivacyPolicies(
			@ModelAttribute("directDepositPrivacyPolicyForm") PrivacyPolicyFormBean directDepositPrivacyPolicyForm,
			@ModelAttribute("taxFormPrivacyPolicyForm") PrivacyPolicyFormBean taxFormPrivacyPolicyForm,
			@ModelAttribute("directDepositTerms") PrivacyPolicyFormBean directDepositTerms,
			@ModelAttribute("taxFormTerms") PrivacyPolicyFormBean taxFormTerms,
			@RequestParam(required = false) final String tab,
			Map<String, Object> map) {
		directDepositPrivacyPolicyForm.setPrivacyPolicy(this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.DIRECT_DEPOSIT_PRIVACY_POLICY));
		taxFormPrivacyPolicyForm.setPrivacyPolicy(this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.TAX_FORM_PRIVACY_POLICY));
		directDepositTerms.setPrivacyPolicy(this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.DIRECT_DEPOSIT_TERMS));
		taxFormTerms.setPrivacyPolicy(this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.TAX_FORM_TERMS));
		map.put("tab", tab);
		return "privacyPolicy";
	}

	/**
	 * 
	 * @param directDepositPrivacyPolicyForm
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value = "/web/privacypolicy/directdeposit.htm", method = RequestMethod.POST)
	public String saveDirectDepositPrivacyPolicy(
			@ModelAttribute("directDepositPrivacyPolicyForm") PrivacyPolicyFormBean directDepositPrivacyPolicyForm) {
		if (directDepositPrivacyPolicyForm.getSubmit().equals("reset"))
			return "redirect:/web/privacypolicies.htm";
		this.commissionService.updatePrivacyPolicy(directDepositPrivacyPolicyForm
				.getPrivacyPolicy(),TermsAndPrivacyPolicy.DIRECT_DEPOSIT_PRIVACY_POLICY);
		return "redirect:/web/privacypolicies.htm?tab=directdepositpolicy";
	}
	
	/**
	 * 
	 * @param directDepositPrivacyPolicyForm
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value = "/web/termsncondition/directdeposit.htm", method = RequestMethod.POST)
	public String saveDirectDepositTermsAndConditions(
			@ModelAttribute("directDepositTerms") PrivacyPolicyFormBean directDepositTerms) {
		if (directDepositTerms.getSubmit().equals("reset"))
			return "redirect:/web/privacypolicies.htm";
		this.commissionService.updatePrivacyPolicy(directDepositTerms
				.getPrivacyPolicy(),TermsAndPrivacyPolicy.DIRECT_DEPOSIT_TERMS);
		return "redirect:/web/privacypolicies.htm?tab=directdepositterms";
	}	
	
	/**
	 * 
	 * @param directDepositPrivacyPolicyForm
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value = "/web/privacypolicy/taxform.htm", method = RequestMethod.POST)
	public String saveTaxFormPrivacyPolicy(
			@ModelAttribute("taxFormPrivacyPolicyForm") PrivacyPolicyFormBean taxFormPrivacyPolicyForm) {
		if (taxFormPrivacyPolicyForm.getSubmit().equals("reset"))
			return "redirect:/web/privacypolicies.htm";
		this.commissionService.updatePrivacyPolicy(taxFormPrivacyPolicyForm
				.getPrivacyPolicy(),TermsAndPrivacyPolicy.TAX_FORM_PRIVACY_POLICY);
		return "redirect:/web/privacypolicies.htm?tab=taxformpolicy";
	}	
	
	/**
	 * 
	 * @param directDepositPrivacyPolicyForm
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value = "/web/termsncondition/taxform.htm", method = RequestMethod.POST)
	public String saveTaxFormTermsAndConditions(
			@ModelAttribute("taxFormTerms") PrivacyPolicyFormBean taxFormTerms) {
		if (taxFormTerms.getSubmit().equals("reset"))
			return "redirect:/web/privacypolicies.htm";
		this.commissionService.updatePrivacyPolicy(taxFormTerms
				.getPrivacyPolicy(),TermsAndPrivacyPolicy.TAX_FORM_TERMS);
		return "redirect:/web/privacypolicies.htm?tab=taxformterms";
	}
	
	/**
	 * 
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value="/privacyPolicy/directdepositpolicy.htm",method=RequestMethod.GET)
	@ResponseBody
	public String getDirectDepositPrivacyPolicy(){
		return this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.DIRECT_DEPOSIT_PRIVACY_POLICY);
	}
	
	/**
	 * 
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value="/privacyPolicy/taxformpolicy.htm",method=RequestMethod.GET)
	@ResponseBody
	public String getTaxFormPrivacyPolicy(){
		return this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.TAX_FORM_PRIVACY_POLICY);
	}
	
	/**
	 * 
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value="/termsandcondition/directdepositpolicy.htm",method=RequestMethod.GET)
	@ResponseBody
	public String getDirectDepositTermsAndCondition(){
		return this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.DIRECT_DEPOSIT_TERMS);
	}
	
	/**
	 * 
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@RequestMapping(value="/termsandcondition/taxformpolicy.htm",method=RequestMethod.GET)
	@ResponseBody
	public String getTaxFormTermsAndCondition(){
		return this.commissionService.getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy.TAX_FORM_TERMS);
	}

}
