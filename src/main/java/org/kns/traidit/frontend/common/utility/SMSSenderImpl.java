package org.kns.traidit.frontend.common.utility;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.kns.traidit.frontend.common.exception.SMSNotSentException;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.stereotype.Service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Service("SMSSender")
public class SMSSenderImpl implements SMSSender{


	@Resource(name="TwilioRestClient")
	private TwilioRestClient client;
	
	private String fromPhoneNo = "+18143726035";

	/**
	 * 
	 * @param phoneNo
	 * @param message
	 * @throws SMSNotSentException
	 * @throws Exception
	 */
	@Override
	public void sendSMS(String phoneNo, String message) throws SMSNotSentException{

	    // Build a filter for the MessageList
		try {
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("Body", message));
		    params.add(new BasicNameValuePair("To", phoneNo));
		    params.add(new BasicNameValuePair("From", this.fromPhoneNo));
		 
		    MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message smsMessage = messageFactory.create(params);
		    System.out.println(smsMessage.getSid());
		} catch (TwilioRestException e) {
			throw new SMSNotSentException();
		}
	    
	    
	}
	
	/**
	 * 
	 * @param phoneNo
	 * @param message
	 * @throws SMSNotSentException
	 * @throws Exception
	 */
	@Override
	public void sendPasswordResetSMS(UserDto user) throws SMSNotSentException{
		String message = "Hi "+user.getUserName()+", Your Traidit Password Reset Token is "+user.getPasswordToken()+". Please dont share this with anyone and it valid only till today";
		String phoneNo = user.getPhoneNo();
	    // Build a filter for the MessageList
		try {
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("Body", message));
		    params.add(new BasicNameValuePair("To", phoneNo));
		    params.add(new BasicNameValuePair("From", this.fromPhoneNo));
		  System.out.println(params.toString());
		    MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message smsMessage = messageFactory.create(params);
		    System.out.println(smsMessage.getSid());
//		 throw new TwilioRestException("asdf", 404);
		} catch (TwilioRestException e) {
			e.printStackTrace();
			throw new SMSNotSentException();
		}
	    
	    
	}
	
}
