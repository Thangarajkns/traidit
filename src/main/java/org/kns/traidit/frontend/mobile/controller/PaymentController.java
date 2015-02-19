package org.kns.traidit.frontend.mobile.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.exception.CommissionNotSavedException;
import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.payments.model.PaymentStatus;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.common.exception.InvalidCardException;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.common.utility.PayPal;
import org.kns.traidit.frontend.common.utility.PaypalCreditCard;
import org.kns.traidit.frontend.payments.dto.ManualPaymentFromBean;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.kns.traidit.frontend.payments.service.PaymentService;
import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;


/**
 * 
 * @author JEEVAN
 * Created by Jeevan on September 23, 2014
 * Controller to handle all Payment Related Process
 *
 */
@Controller("paymentController")
public class PaymentController{

	private static Logger log=Logger.getLogger(PaymentController.class);	

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="paymentService")
	private PaymentService paymentService;
	
	@Resource(name = "commissionService")
	private CommissionService commissionService; 
	
	/*added by Soujanya on july 28,2014
	 * method to save user selected plan and update user
	 * also to handle payment through credit card
	 */
	@RequestMapping("/handlepayment.htm")
	@ResponseBody
	public String handlePayment(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside handlePayment()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			
			Integer userId=json.getInt("userid");
			Integer planId=json.getInt("planid");
			UserDto user = this.userService.generatePaymentAuthenticationToken(userId);
			PlansDto plan = this.userService.getPlanById(planId);
			
			//obj.accumulate("paymenturl","https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_xclick&business=thangaraj-facilitator@knstek.com&item_name=testing&currency_code=USD&amount=0.01&no_shipping=0&no_note=1&mrb=3FWGC6LFTMTUG&bn=IC_Sample&return=http://54.83.5.80:7090/traidit/paymentHandler.htm?paymentToken="+user.getPaymentAuthenticationToken());
			if(plan.getPrice()>0){
				Properties properties = new Properties();
				String propFileName = "payment.properties";
		        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		        properties.load(inputStream);
		        StringBuilder queryString = new StringBuilder();
		        queryString.append("?paymentToken=").append(user.getPaymentAuthenticationToken()).append("&planId=").append(planId);
				StringBuilder url = new StringBuilder(properties.getProperty("targetURL"));
				url.append("?cmd=_xclick&business=").append(properties.getProperty("businessEmail"));
				url.append("&lc=").append("US");
				url.append("&no_note=").append("0");
				url.append("&currency_code=").append("USD");
				url.append("&item_name=").append("plan subscription");
				url.append("&amount=").append(plan.getPrice());
				
				url.append("&cancel_return=").append(URLEncoder.encode(properties.getProperty("cancelReturnUrl")+queryString.toString(), "UTF-8"));
				url.append("&item_number=").append("");
				url.append("&return=").append(URLEncoder.encode(properties.getProperty("returnUrl")+queryString.toString(), "UTF-8"));
				//url += "&invoice=123456";
				obj.accumulate("paymenturl", url);
			}
			else{
				obj.accumulate("paymenturl", "");
				this.userService.savePlanOfUser(userId,planId);
				user = this.userService.getUserbyUserId(userId);
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
				obj.accumulate("accountExpiryDate", dateFormat.format(user.getAccountExpiryDate()));
			}
			obj.accumulate("status", "PLAN SAVED");
			obj.accumulate("userid", user.getUserId());
		}
		catch(PlanNotFoundException e){
			log.error("Plan Not Found"+e.toString());
			String message="Plan Not Found";
			obj.accumulate("status", message);
		}
		catch(UserNotSavedOrUpdatedException e){
			log.error("Error In Saving Payment Details"+e.toString());
			String message="Error in Saving Or Updating User";
			obj.accumulate("status", message);
		}
		catch(UserNotFoundException e){
			log.error(e.toString());
			String message="User Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
			String message="Error While Initiating Subcription Payment";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	/**
	 * This method accepts credit/debit card details of particular user along with the plan details he wants to upgrade/renew to. 
	 * After making the corressponding payment from the provided card, it sets that plan to given user.
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @author Thangaraj
	 * @since 03-12-2014
	 */
	@RequestMapping(value="/handleCardPayment.htm")
	@ResponseBody
	public String handleCardPayment(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside handleCardPayment()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			Integer userId			= json.getInt("userid");
			Integer planId			= json.getInt("planid");
			String cardNo			= json.getString("cardnumber");
			String cvv2 			= json.getString("cvv2");
			String cardexpirydate	= json.getString("expirymonth")+json.getString("expiryyear");
			String cardtype			= json.getString("cardtype");

	        UserDto user = this.userService.getUserbyUserId(userId);
			Properties properties = new Properties();
			String propFileName = "payment.properties";
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	        properties.load(inputStream);

	    	PaypalCreditCard card = new PaypalCreditCard();
			card.setCardHolderFirstName(user.getFirstName());
			card.setCardHolderLastName(user.getLastName());
			card.setCardHolderStreet(user.getStreet());
			card.setCardHolderCity(user.getCity());
			card.setCardHolderState(user.getState());
			card.setCardHolderZip(user.getZip());
			card.setCardNo(cardNo);
			card.setCardCvv2(cvv2);
			card.setCardExpDate(cardexpirydate);
			card.setCardType(cardtype);
	        PayPal.setCardDetails(card);

	        PlansDto plan = this.userService.getPlanById(planId);
			Map<String, String> map = PayPal.makePayment(properties,new BigDecimal(plan.getPrice()));
			
	        user.setPaymentStatus(PaymentStatus.COMPLETED.toString());
	        //Added by Jeevan on Decmeber 31.
	        Date date=user.getAccountCreationDate();
	        Calendar cal=Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.MONTH, 1);
	        user.setAccountExpiryDate(cal.getTime());
	        
	        
			UserSubscriptionPaymentsDto payment = new UserSubscriptionPaymentsDto();
			
//			update account expiration date
			String status = "FAILED";	
			
			System.out.println("MAP " +map);
			if(map.get("ACK").equals("Success")){
				status = "COMPLETED";
				this.userService.savePlanOfUser(userId,planId);
			}
			payment.setAmount(new BigDecimal(map.get("AMT")));
			payment.setCurrency(map.get("CURRENCYCODE"));
			payment.setPaidDate(new Date());
			payment.setPlan(plan);
			payment.setPaidDate(new Date());
			payment.setStatus(status);
			payment.setTransactionId(map.get("TRANSACTIONID"));
			payment.setUser(user);
			payment.setSource("CARD");
			payment.setSubscriptionId(this.paymentService.saveOrUpdatePayment(payment));
			
			if(status.equals("COMPLETED")){
				obj.accumulate("status", "Plan upgraded successfully");
				SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");				
				obj.accumulate("accountexpirydate", sdf.format(user.getAccountExpiryDate()));
				try {
					this.commissionService.sendCommissionToReferer(user, new BigDecimal(plan.getPrice()));
				} catch (CommissionNotSavedException e) {
					log.error("Error wile saving commission : "+e.toString());
					System.out.println("Error wile saving commission : "+e.toString());
				}
				user.setLastPaymentId(payment.getSubscriptionId());
				this.userService.saveOrUpdateTraiditUser(user);
			}
			else{
				obj.accumulate("status", "Payment failed");
			}
		} catch (IOException e) {
			String message="Error while processing the request";
			log.error("IOException : "+message);
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {
			String message="no such user exists";
			log.error("UserNotFoundException : "+message);
			obj.accumulate("status", message);
		} catch (PlanNotFoundException e) {
			String message="no such plan exists";
			log.error("PlanNotFoundException : "+message);
			obj.accumulate("status", message);
		} catch (RoleNotFoundException e) {
			String message="no such role exists";
			log.error("RoleNotFoundException : "+message);
			obj.accumulate("status", message);
		} catch (UserNotSavedOrUpdatedException e) {
			String message="could not save or update User, please try again later";
			log.error("UserNotSavedOrUpdatedException : "+message);
			obj.accumulate("status", message);
		} catch (InvalidCardException e) {
			String message="Please check your card details";
			log.error("InvalidCardException : "+message);
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * Payment response handler : handles the response for the payment from paypal site and updates the payment status of the user in DB
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param paymentAuthenticationToken an unique token generated and sent with the payment url to authenticate and identify the correct user of given payment
	 * @param transactionId unique id generated by paypal for this specific transaction
	 * @param status payment status
	 * @param amount amount of the plan user is supposed to pay
	 * @param currency code representing the currency
	 * @return String data which has to be displayed on the success screen
	 * @author Thangaraj(KNSTEK)
	 * @since 02-09-2014
	 */
	@ResponseBody
	@RequestMapping(value="/paymentHandler.htm")
	public String paymentHandler(HttpServletRequest request,HttpServletResponse response){
		
		log.info("inside paymentHandler()");
		System.out.println("inside paymentHandler()");

		String paymentAuthenticationToken = (String)request.getParameter("paymentToken");
		Integer planId = Integer.parseInt((String)request.getParameter("planId"));
		String transactionId = (String)request.getParameter("tx");
		String status = (String)request.getParameter("st");
		String amount = (String)request.getParameter("amt");
		String currency = (String)request.getParameter("cc");
		if(status == null){
			status = status != null?status:PaymentStatus.CANCELLED.toString();
			amount = "0.0";
			currency = "";
			transactionId = "";
		}
		try{
			UserDto user = this.userService.getUserByPaymentAuthenticationToken(paymentAuthenticationToken);
			UserSubscriptionPaymentsDto payment = new UserSubscriptionPaymentsDto();
			PlansDto plan = this.userService.getPlanById(planId);
			if(status.equals("Completed")){
//				update account expiration date and plan
				this.userService.savePlanOfUser(user.getUserId(),plan.getPlanId());
				user = this.userService.getUserbyUserId(user.getUserId());
				try {
					this.commissionService.sendCommissionToReferer(user, new BigDecimal(plan.getPrice()));
				} catch (CommissionNotSavedException e) {
					log.error("Error wile saving commission : "+e.toString());
					System.out.println("Error wile saving commission : "+e.toString());
				}
			}
			payment.setAmount(new BigDecimal(amount));
			payment.setCurrency(currency);
			payment.setPaidDate(new Date());
			payment.setPlan(plan);
			payment.setStatus(status);
			payment.setTransactionId(transactionId);
			payment.setUser(user);
			payment.setSource("PAYPAL");
			payment.setSubscriptionId(this.paymentService.saveOrUpdatePayment(payment));
			user.setPaymentStatus(PaymentStatus.COMPLETED.toString());
			user.setLastPaymentId(payment.getSubscriptionId());
			this.userService.saveOrUpdateTraiditUser(user);
		}
		catch(UserNotFoundException e){
			log.error(e.toString());
			System.out.println("UserNotFoundException : No match found for paymentAuthenticationToken");
			return "Payment Processing failed";
		} catch (RoleNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (UserNotSavedOrUpdatedException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (PlanNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		}
		finally{
			log.info("paymentAuthenticationToken"+paymentAuthenticationToken);
			log.info("transactionId"+transactionId);
			log.info("status"+status);
			log.info("amount"+amount);
			log.info("currency"+currency);
			System.out.println("paymentAuthenticationToken"+paymentAuthenticationToken);
			System.out.println("transactionId"+transactionId);
			System.out.println("status"+status);
			System.out.println("amount"+amount);
			System.out.println("currency"+currency);
		}
		if(!status.equals("Completed"))
			return "payment "+status;
		return "paymentSuccess";
	}
	
	
	
	/**
	 * sends back the current payment status of given user.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return JSON Object holding userId and paymentStatus
	 * @throws JSONException 
	 * @throws JSONException if there is any error on accumulating data to JSONObject.
	 * @author Thangaraj(KNSTEK)
	 * @since 02-09-2014
	 */
	@RequestMapping(value="/paymentStatus.htm")
	@ResponseBody
	public String paymentStatus(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside paymentStatus()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			String userId = json.getString("userId");
			UserDto user = this.userService.getUserbyUserId(Integer.parseInt(userId));
			String status = user.getPaymentStatus();
			if(status.equals("COMPLETED"))
			{
				if(user.getLastPaymentId()!= null ){
					UserSubscriptionPaymentsDto payment = this.paymentService.getPaymentById(user.getLastPaymentId());
					if(payment.getStatus().equals("Pending"))
						obj.accumulate("paymentStatus", "PENDING");
					else if(payment.getStatus().equals("Completed"))
						obj.accumulate("paymentStatus", "COMPLETED");
					else if(payment.getStatus().equals("CANCELLED"))
						obj.accumulate("paymentStatus", "CANCELLED");
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					obj.accumulate("accountExpiryDate", formatter.format(user.getAccountExpiryDate()));
				}
				else
					obj.accumulate("paymentStatus", "NIL");
			}
			else if(status.equals("PENDING"))
			{
				obj.accumulate("paymentStatus", "");
			}
			else if(status.equals("INACTIVE"))
			{
				obj.accumulate("paymentStatus", "");
			}
			obj.accumulate("userId", userId);
			return obj.toString();
		}
		catch(UserNotFoundException e){
			log.error(e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Invalid user id");
			return obj.toString();
		}
		catch(PaymentNotFoundException e){
			log.error(e.toString());
			e.printStackTrace();
			obj.accumulate("status", "No payments found");
			return obj.toString();
		}
		catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Some error occured in payment");
			return obj.toString();
		}
	}
	
	@RequestMapping(value="/web/user/makemanualpayment.htm",method = RequestMethod.POST)
	public String addManualPayment(@RequestParam(required = false)Integer userId,@ModelAttribute("makeManualPayment")ManualPaymentFromBean form,Map<String,Object> map){
		if(userId==null)
			return "redirect:/web/user/listUsers.htm";
		try {
			form.setUser(this.userService.getUserbyUserId(userId));
			map.put("plans", this.userService.getAllPlans());
		} catch (UserNotFoundException e) {
			return "redirect:/web/user/listUserPayments.htm?id="+userId;
		} catch (PlanNotFoundException e) {
			return "redirect:/web/user/listUserPayments.htm?id="+userId;
		}
		return "makeManualPayment";
	}
	
	@RequestMapping(value="/web/user/processmanualpayment.htm",method=RequestMethod.POST)
	public String processManualPayment(@ModelAttribute("makeManualPayment")ManualPaymentFromBean form,Map<String,Object> map){
		if(form.getSubmitOption().equals("Cancel"))
			return "redirect:/web/user/listUserPayments.htm?id="+form.getUser().getUserId();
		try{
			UserDto user = this.userService.getUserbyUserId(form.getUser().getUserId());
			UserSubscriptionPaymentsDto payment = new UserSubscriptionPaymentsDto();
			PlansDto plan = this.userService.getPlanById(form.getPlan().getPlanId());
			this.userService.savePlanOfUser(user.getUserId(),plan.getPlanId());
			user = this.userService.getUserbyUserId(user.getUserId());
			if(form.getPayCommissions()){
				try {
					this.commissionService.sendCommissionToReferer(user, new BigDecimal(plan.getPrice()));
				} catch (CommissionNotSavedException e) {
					log.error("Error wile saving commission : "+e.toString());
					System.out.println("Error wile saving commission : "+e.toString());
				}
			}
			payment.setAmount(new BigDecimal(plan.getPrice()));
			payment.setCurrency("USD");
			payment.setPaidDate(new Date());
			payment.setPlan(plan);
			payment.setStatus("Completed");
			payment.setTransactionId(form.getTransactionId());
			payment.setUser(user);
			payment.setSource("MANUAL_PAYMENT");
			payment.setSubscriptionId(this.paymentService.saveOrUpdatePayment(payment));
			user.setPaymentStatus(PaymentStatus.COMPLETED.toString());
			user.setLastPaymentId(payment.getSubscriptionId());
			this.userService.saveOrUpdateTraiditUser(user);
		}
		catch(UserNotFoundException e){
			log.error(e.toString());
			System.out.println("UserNotFoundException : No match found for paymentAuthenticationToken");
			return "Payment Processing failed";
		} catch (RoleNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (UserNotSavedOrUpdatedException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (PlanNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		}
		return "redirect:/web/user/listUserPayments.htm?id="+form.getUser().getUserId();
	}
	
	@RequestMapping(value="/web/user/extendtrailplan.htm",method = RequestMethod.POST)
	public String extendTrialPlan(@RequestParam(required = false)Integer userId,@ModelAttribute("extendtrialform")ManualPaymentFromBean form,Map<String,Object> map){
		if(userId==null)
			return "redirect:/web/user/listUsers.htm";
		try {
			form.setUser(this.userService.getUserbyUserId(userId));
			form.setAccountExpirationDate(form.getUser().getAccountExpiryDate());
			map.put("plans", this.userService.getAllPlans());
		} catch (UserNotFoundException e) {
			return "redirect:/web/user/listUserPayments.htm?id="+userId;
		} catch (PlanNotFoundException e) {
			return "redirect:/web/user/listUserPayments.htm?id="+userId;
		}
		return "extendtrailplan";
	}
	
	@RequestMapping(value="/web/user/processextendtrial.htm",method=RequestMethod.POST)
	public String processExtendTrialPlan(@ModelAttribute("extendtrialform")ManualPaymentFromBean form,Map<String,Object> map){
		if(form.getSubmitOption().equals("Cancel"))
			return "redirect:/web/user/listUserPayments.htm?id="+form.getUser().getUserId();
		try{
			UserDto user = this.userService.getUserbyUserId(form.getUser().getUserId());
			UserSubscriptionPaymentsDto payment = new UserSubscriptionPaymentsDto();
			PlansDto plan = this.userService.getPlanById(form.getUser().getPlans().getPlanId());
			this.userService.savePlanOfUser(user.getUserId(),plan.getPlanId());
			user = this.userService.getUserbyUserId(user.getUserId());
			payment.setAmount(new BigDecimal(plan.getPrice()));
			payment.setCurrency("USD");
			payment.setPaidDate(new Date());
			payment.setPlan(plan);
			payment.setStatus("Completed");
			payment.setTransactionId(form.getTransactionId());
			payment.setUser(user);
			payment.setSource("MANUAL_PAYMENT");
			payment.setSubscriptionId(this.paymentService.saveOrUpdatePayment(payment));
			user.setPaymentStatus(PaymentStatus.COMPLETED.toString());
			user.setLastPaymentId(payment.getSubscriptionId());
			user.setAccountExpiryDate(form.getAccountExpirationDate());
			this.userService.saveOrUpdateTraiditUser(user);
		}
		catch(UserNotFoundException e){
			log.error(e.toString());
			System.out.println("UserNotFoundException : No match found for paymentAuthenticationToken");
			return "Payment Processing failed";
		} catch (RoleNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (UserNotSavedOrUpdatedException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (PlanNotFoundException e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
			return "Payment Processing failed";
		}
		return "redirect:/web/user/listUserPayments.htm?id="+form.getUser().getUserId();
	}
}
