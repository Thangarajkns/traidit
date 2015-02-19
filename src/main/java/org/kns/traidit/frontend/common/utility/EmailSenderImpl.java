package org.kns.traidit.frontend.common.utility;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.kns.traidit.backend.commission.model.CommissionOfInActiveAccount;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service("emailSender")
public class EmailSenderImpl implements EmailSender{
	
	private static Logger log=Logger.getLogger(EmailSenderImpl.class);
	
	/**
	 * handles sending mail. Autowired using the bean defined in name mailSender
	 * @see applicationContext.xml
	 */
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * holds the server url (used to build the forgot password link in mail body)
	 * @see applicationContext.xml
	 */
	private String serverHost;
	/**
	 * Template engine. Autowired using the bean defined in the name velocityEngine
	 * @see applicationContext.xml
	 */
	@Autowired
	private VelocityEngine velocityEngine;

	
	public String getServerHost() {
		return serverHost;
	}


	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}


	@Override
	public void sendForgotPasswordMail( final UserDto user)
			throws MailNotSentException {
		System.out.println("EmailSenderImpl -> sendForgotPasswordMail() ");
		log.info("EmailSenderImpl -> sendForgotPasswordMail()");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
				message.setTo(user.getEmail());
				message.setSubject("Traidit Password Reset Link");
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("email", user.getEmail());
				map.put("firstname", user.getFirstName());
				map.put("passwordToken", user.getPasswordToken());
				map.put("user", user);
				map.put("serverURL", serverHost);
				System.out.println(map.toString());
				String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "org/kns/traidit/templates/forgotPassword.vm","UTF-8", map);
				message.setText(content);
        		System.out.println(content);
        		log.info(content);
			}
		};
		
		this.mailSender.send(preparator);
		
	}

	/**
	 * 
	 * @param commission
	 * @throws MailNotSentException
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 16-Dec-2014
	 */
	public void intimateLostCommission(final CommissionOfInActiveAccount commission)throws MailNotSentException{

		System.out.println("EmailSenderImpl -> sendForgotPasswordMail() ");
		log.info("EmailSenderImpl -> sendForgotPasswordMail()");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
				message.setTo(commission.getbenificiary().getEmail());
				message.setSubject("You missed your JUICE on Traidit");
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("commission", commission);
				String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "org/kns/traidit/templates/intimateLostCommission.vm","UTF-8", map);
				message.setText(content);
        		System.out.println(content);
        		log.info(content);
			}
		};
		this.mailSender.send(preparator);
	}
}
