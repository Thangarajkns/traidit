/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.frontend.commission.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.dao.CommissionDao;
import org.kns.traidit.backend.commission.dao.CommissionDaoImpl;
import org.kns.traidit.backend.commission.exception.CommissionNotSavedException;
import org.kns.traidit.backend.commission.exception.CommissionPaymentNotSavedException;
import org.kns.traidit.backend.commission.exception.PrivacyPolicyNotFoundException;
import org.kns.traidit.backend.commission.model.CommissionOf;
import org.kns.traidit.backend.commission.model.CommissionOfActiveAccount;
import org.kns.traidit.backend.commission.model.CommissionOfInActiveAccount;
import org.kns.traidit.backend.commission.model.CommissionPayment;
import org.kns.traidit.backend.commission.model.CommissionPaymentType;
import org.kns.traidit.backend.commission.model.PrivacyPolicy;
import org.kns.traidit.backend.commission.model.TermsAndPrivacyPolicy;
import org.kns.traidit.backend.payments.model.PaymentStatus;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.CommissionPaymentsNotFoundException;
import org.kns.traidit.backend.user.exception.CommissionsNotFoundException;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.commission.dto.CommissionDto;
import org.kns.traidit.frontend.commission.dto.CommissionPaymentDto;
import org.kns.traidit.frontend.common.utility.EmailSender;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.kns.traidit.frontend.payments.service.PaymentService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Transactional
@Service("commissionService")
public class CommissionServiceImpl implements CommissionService {

	private static final Logger log = Logger.getLogger(CommissionServiceImpl.class);
	
	@Resource(name="commissionDao")
	private CommissionDao commissionDao;
	
	@Resource(name = "userDao")
	private UserDao userDao;
	
	@Resource(name="emailSender")
	private EmailSender emailSender; 
	
	@Resource(name="paymentService")
	private PaymentService paymentService;
	
	public static Integer totalNoOfResults;
	
	public void sendCommissionToReferer(UserDto payerDto,BigDecimal amount) throws UserNotFoundException, CommissionNotSavedException{
		TraidItUser payer = this.userDao.getUserbyUserId(payerDto.getUserId());
		BigDecimal commissionAmount = sendCommission(payer, payer, amount, CommissionOf.SUBSCRIPTION);
		System.out.println(commissionAmount);
		getCommissionByUser(new UserDto());
	}
	
	/**
	 * 
	 * @param sourcePayer
	 * @param payer
	 * @param amount
	 * @param commissionOf
	 * @throws UserNotFoundException
	 * @throws CommissionNotSavedException
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Dec-2014
	 */
	private BigDecimal sendCommission(TraidItUser sourcePayer,TraidItUser payer,BigDecimal amount,CommissionOf commissionOf) throws UserNotFoundException, CommissionNotSavedException{
		if(payer.getReferral()==null)
			return new BigDecimal(0);
		TraidItUser referer = this.userDao.getUserbyUserId(payer.getReferral().getUserId());
		BigDecimal allotedCommission, commissionAmount = caluculateCommissionAmount(amount);
		if(!referer.getIsExpired()&&referer.getIsEnabled()&&!referer.getIsLocked()&&referer.getAccountExpiryDate().after(new Date())){
			CommissionOfActiveAccount commission = new CommissionOfActiveAccount();
			commission.setbenificiary(referer);
			commission.setpayer(payer);
			commission.setSourcePayer(sourcePayer);
			commission.setCommissionAmount(commissionAmount);
			commission.setCommissionOf(commissionOf);
			commission.setDate(new Date());
			this.commissionDao.saveOrUpdateCommissionOfActiveAccount(commission);
			allotedCommission = commissionAmount;
		}
		else{
			CommissionOfInActiveAccount commission = new CommissionOfInActiveAccount();
			commission.setbenificiary(referer);
			commission.setpayer(payer);
			commission.setSourcePayer(sourcePayer);
			commission.setCommissionAmount(commissionAmount);
			commission.setCommissionOf(commissionOf);
			commission.setDate(new Date());
			this.commissionDao.saveOrUpdateCommissionOfInActiveAccount(commission);
			try {
				this.emailSender.intimateLostCommission(commission);
			} catch (MailNotSentException e) {
				System.out.println("could not send mail to intimate lost commission of commissionId :"+commission.getId());
				log.error("could not send mail to intimate lost commission of commissionId :"+commission.getId());
			}
			allotedCommission = new BigDecimal(0);
		}
		return allotedCommission.add(sendCommission(sourcePayer, referer, commissionAmount, CommissionOf.COMMISSION));
	}
	
	/**
	 * 
	 * @param amount
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Dec-2014
	 */
	private BigDecimal caluculateCommissionAmount(BigDecimal amount){
		return amount.divide(new BigDecimal(10));
	}
	
	/**
	 * 
	 * @param userDto
	 * @return
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Dec-2014
	 */
	public BigDecimal getCommissionByUser(UserDto userDto){
		TraidItUser user = new TraidItUser(userDto.getUserId());
		BigDecimal commissionAmountEarned = this.commissionDao.getTotalCommissionEarned(user);
		BigDecimal commissionAmountUtilised = this.commissionDao.getTotalCommissionPaid(user);
		return commissionAmountEarned.subtract(commissionAmountUtilised);
	}
	
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 17-Dec-2014
	 */
	public BigDecimal getCommissionByDate(Date fromDate,Date toDate){
		return this.commissionDao.getCommissionByDate(fromDate,toDate);
	}

	/**
	 * 
	 * @param userDto
	 * @return
	 * @throws CommissionPaymentNotSavedException
	 * @throws UserNotFoundException
	 * @throws CommissionNotSavedException
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @throws PlanNotFoundException 
	 * @since 16-Dec-2014
	 */
	public Boolean paySubscriptionFromCommission(UserDto userDto) throws CommissionPaymentNotSavedException, UserNotFoundException, CommissionNotSavedException, PlanNotFoundException{
		log.info("paySubscriptionFromCommission("+userDto.getUserId()+") initiated");
		Boolean result = false;
		BigDecimal commissionEarned = this.getCommissionByUser(userDto);
		BigDecimal amount = new BigDecimal(userDto.getPlans().getPrice());
		Date currentDate = new Date();
		if(commissionEarned.compareTo(amount) >= 0){
			CommissionPayment payment = new CommissionPayment();
			payment.setAmount(amount);
			payment.setBenificiary(new TraidItUser(userDto.getUserId()));
			payment.setDate(currentDate);
			payment.setPaymentType(CommissionPaymentType.SUBSCRIPTION);
			this.commissionDao.saveOrUpdateCommissionPayment(payment);
			
			UserSubscriptionPaymentsDto userPayment = new UserSubscriptionPaymentsDto();
			userPayment.setUser(userDto);
			userPayment.setStatus(PaymentStatus.COMPLETED.toString());
			userPayment.setAmount(amount);
			userPayment.setCurrency("USD");
			userPayment.setPaidDate(currentDate);
			userPayment.setPlan(userDto.getPlans());
			userPayment.setSource("Commission");
			userPayment.setTransactionId(payment.getId().toString());
			this.paymentService.saveOrUpdatePayment(userPayment);
			
			this.sendCommissionToReferer(userDto, amount);
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return

	 * @return ArrayList<CommissionOfActiveAccount>
	 * @author Thangaraj(KNSTEK)
	 * @since 25-Dec-2014
	 */
	public ArrayList<CommissionDto> getCommissionsOfAllUsers(String sortBy,String sortOrder,Integer limit,Integer startIndex){ 
		ArrayList<CommissionDto> commissionDtoList = new ArrayList<CommissionDto>();
		ArrayList<CommissionOfActiveAccount> commissionList =  this.commissionDao.getCommissionsOfAllUsers(sortBy, sortOrder, limit, startIndex);
		totalNoOfResults = CommissionDaoImpl.totalNoOfResults;
		for(CommissionOfActiveAccount commission:commissionList){
			CommissionDto commissionDto = new CommissionDto();
			commissionDto.setBenificiary(UserDto.populateUserDto(commission.getbenificiary()));
			commissionDto.setCommissionAmount(commission.getCommissionAmount());
			commissionDtoList.add(commissionDto);
		}
		return commissionDtoList;
	}

	public ArrayList<CommissionPaymentDto> getPaymentsOfUser(Integer userId,String sortBy,String sortOrder,Integer limit,Integer startIndex){
		ArrayList<CommissionPayment> payments = this.commissionDao.getPaymentsOfUser(userId,sortBy, sortOrder, limit, startIndex);
		totalNoOfResults = CommissionDaoImpl.totalNoOfResults;
		ArrayList<CommissionPaymentDto> paymentDtos = new ArrayList<CommissionPaymentDto>();
		for(CommissionPayment payment:payments){
			paymentDtos.add(CommissionPaymentDto.populateCommissionPaymentDto(payment));
		}
		return paymentDtos;
	}
	
	/**
	 * 
	 * @param userId
	 * @return ArrayList<CommissionDto>
	 * @author Bhagya
	 * @throws UserNotFoundException 
	 * @since 05-Jan-2015
	 * 
	 * Method For getting commisions, for Particular user based on userId
	 */
	public ArrayList<CommissionDto> getCommissionsOfUser(Integer userId) throws UserNotFoundException{
		ArrayList<CommissionDto> commissionDtoList=new ArrayList<CommissionDto>();
		ArrayList<CommissionOfActiveAccount> commissionList=this.commissionDao.getCommissionsOfUser(userId);
		for(CommissionOfActiveAccount commission:commissionList){
			CommissionDto commissionDto = new CommissionDto();
			commissionDto.setBenificiary(UserDto.populateUserDto(commission.getbenificiary()));
			commissionDto.setCommissionAmount(commission.getCommissionAmount());
			commissionDto.setId(commission.getId());
			commissionDto.setCommissionOf(commission.getCommissionOf());
			commissionDtoList.add(commissionDto);
		}
		return commissionDtoList;
		
	}


	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return BigDecimal
	 * @author Thangaraj(KNSTEK)
	 * @since 05-Feb-2015
	 */
	public BigDecimal getRevenueAmountByDate(Date fromDate,Date toDate){
		BigDecimal subscriptionAmount 	= this.paymentService.getSubscriptionAmountByDate(fromDate, toDate);
		BigDecimal commissionAmount 	= this.getCommissionByDate(fromDate, toDate);
		return subscriptionAmount.subtract(commissionAmount);
	}

	/**
	 * 
	
	
	/**
	 * 
	 * @param privacyPolicy
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 11-Feb-2015
	 */
	@Override
	public Integer updatePrivacyPolicy(String privacyPolicy,TermsAndPrivacyPolicy type){
		PrivacyPolicy policy;
		try {
			policy = this.commissionDao.getPrivacyPolicy(type);
			policy.setUpdatedOn(new Date());
		} catch (PrivacyPolicyNotFoundException e) {
			policy = new PrivacyPolicy();
			policy.setType(type.toString());
			policy.setCreatedOn(new Date());
			policy.setUpdatedOn(policy.getCreatedOn());
		}
		policy.setPrivacyPolicy(privacyPolicy);
		return this.commissionDao.updatePrivacyPolicy(policy);
	}
	
	/**
	 * 
	 * @param type
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 16-Feb-2015
	 */
	public String getTermsAndPrivacyPolicy(TermsAndPrivacyPolicy type){

		String privacyPolicy;
		try {
			PrivacyPolicy policy = this.commissionDao.getPrivacyPolicy(type);
			privacyPolicy = policy.getPrivacyPolicy();
		} catch (PrivacyPolicyNotFoundException e) {
			privacyPolicy = "";
		}
		return privacyPolicy;
	}
	

	/**
	 * Created By Bhagya On Feb 17th,2015
	 * @return
	 * @throws CommissionsNotFoundException
	 * 
	 * Method For getting the Monthly commissions of all users
	 * @throws CommissionPaymentsNotFoundException 
	 * @throws UserNotFoundException 
	 * 
	 * This Method Returns the List Of Monthly Commissions..which commissions are need to pay by admin
	 * 1.we are setting the current month startdate and end date from calendar object
	 * 2.we are getting the monthly commissions From commission of Active Account table i.e..commission table
	 * 3.we are getting the monthly commissions From Commission payment table
	 * 	if user exist in commission payments list means
	 * 		i.For getting the actual commission(Commission To Be Paid By Admin)=sustracting  the commission-commissionpayment
	 * 	else we are adding the same amoount of commission
	 * 4.we populated to commission Dto and then return commissionDto 
	 */


	public ArrayList<CommissionDto> getMonthlyCommissionsOfAllUsers() throws CommissionsNotFoundException, UserNotFoundException{

		log.info("inside getMonthlyCommissionsOfAllUsers()");
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		Integer dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);
		cal.add(Calendar.DATE,-(dayOfMonth-1));
		Date startDate=cal.getTime();
		cal.add(Calendar.MONTH,+1);
		Date endDate=cal.getTime();			
		Map<Integer, BigDecimal> commissions=this.commissionDao.getCommissionsOfAllUsersBetweenDays(startDate, endDate);
		Map<Integer, BigDecimal> commissionPayments = new HashMap<Integer, BigDecimal>();
		try {
			commissionPayments = this.commissionDao.getCommissionPaymentsOfAllUsersBetweenDays(startDate, endDate);
		} catch (CommissionPaymentsNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<Integer, BigDecimal> actualCommissionmMap=new HashMap<Integer, BigDecimal>();
			for(Entry commission:commissions.entrySet()){
					if(commissionPayments.containsKey(commission.getKey())){
							BigDecimal actualCommission = ((BigDecimal)commission.getValue()).subtract(commissionPayments.get(commission.getKey()));
							actualCommissionmMap.put((Integer)commission.getKey(),actualCommission);
					}
					else{
						actualCommissionmMap.put((Integer)commission.getKey(),(BigDecimal)commission.getValue());
					}
			
			}
			ArrayList<CommissionDto> commissionDtoList=new ArrayList<CommissionDto>();
			for(Entry actualComm:actualCommissionmMap.entrySet()){
				CommissionDto commissionDto=new CommissionDto();
				TraidItUser user=this.userDao.getUserbyUserId((Integer)actualComm.getKey());
				commissionDto.setBenificiary(UserDto.populateUserDto(user));
				commissionDto.setCommissionAmount((BigDecimal)actualComm.getValue());
				commissionDtoList.add(commissionDto);
			}
		
		
		return commissionDtoList;
	}
	/**
	 * Created By Bhagya On Feb 18th,2015
	 * @return
	 * @throws CommissionsNotFoundException
	 * @throws UserNotFoundException
	 * 
	 * Method For getting the commissions From start Of Year To Till date
	 */
	
	public ArrayList<CommissionDto> getCommissionsFromStartOfYearToToday() throws CommissionsNotFoundException, UserNotFoundException{
		log.info("inside getCommissionsFromStartOfYearToToday()");
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 0);
		Date endDate=cal.getTime();
		Integer dayOfYear=cal.get(Calendar.DAY_OF_YEAR);
		cal.add(Calendar.DATE,-(dayOfYear-1));
		Date startDate=cal.getTime();		
		Map<Integer, BigDecimal> commissions=this.commissionDao.getCommissionsOfAllUsersBetweenDays(startDate, endDate);
		ArrayList<CommissionDto> commissionDtoList=new ArrayList<CommissionDto>();
		for(Entry commission:commissions.entrySet()){
			CommissionDto commissionDto=new CommissionDto();
			TraidItUser user=this.userDao.getUserbyUserId((Integer)commission.getKey());
			commissionDto.setBenificiary(UserDto.populateUserDto(user));
			commissionDto.setCommissionAmount((BigDecimal)commission.getValue());
			commissionDtoList.add(commissionDto);

		}
		return commissionDtoList;
	}
	
}
