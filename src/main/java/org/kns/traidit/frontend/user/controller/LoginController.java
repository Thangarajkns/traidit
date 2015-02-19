package org.kns.traidit.frontend.user.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("loginController")
public class LoginController {

	private Logger log = Logger.getLogger(LoginController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping(value="/web/login.htm",method=RequestMethod.GET)
	public String initiateLogin(Map<String, Object> map){
		System.out.println("inside login Controller");
		try{
			return "login";
		}
		catch(Exception e){
			System.out.println("login page error");
			return "error";
		}
	}
	
	@RequestMapping(value="/web/loggedin.htm",method=RequestMethod.GET)
	public String onLoginSuccess(Map<String, Object> map){
		try{
			return "loggedin";
		}
		catch(Exception e){
			System.out.println("loggedin page error");
			return "error";
		}
	}
		
	@RequestMapping(value="/web/forgotpassword.htm",method=RequestMethod.GET)
	public String onForgotPassword(Map<String, Object> map){
		try{
			return "forgotPassword";
		}
		catch(Exception e){
			System.out.println("forgotPassword page error");
			return "error";
		}
	}
	
	@RequestMapping(value="/web/forgotpassword.htm",method=RequestMethod.POST)
	public String processForgotPassword(@RequestParam("email") String email,Map<String,Object> map){
		System.out.println("LoginController -> processForgotPassword()");
		try{
			if(!email.equals("")){
				Integer mailSent = this.userService.sendPasswordResetMail(email);
				String msg= "Password rest link has been sent to your registered email, please check your mail.";
				map.put("sucessMessage", msg);
				map.put("title", "Reset Password");
				return "login";
			}
		}
		catch(MailNotSentException e){
			System.out.println("Mail not sent Exception");
			e.printStackTrace();
			String msg = "Couldn't send mail of password reset link";
			map.put("title", "Mail not sent");
			map.put("errorMessage", msg);
			return "login";
		}
		catch(UserNotFoundException e){
			System.out.println("Sorry this mail id is not registered");
			map.put("errorMessage", "Sorry this mail id is not registered");
		}
		catch(Exception e){
			log.error("Exception on processForgotPassword"+ e);
			System.out.println("Exception on processForgotPassword"+ e);
			map.put("errorMessage", "Sorry, error occured");
		}
		return "forgotPassword";
	}
	
	@RequestMapping(value="/web/resetpassword.htm",method=RequestMethod.GET)
	public String initiateResetPassword(@RequestParam("token") String token,Map<String,Object> map){
		try{
			Integer userId = this.userService.getUserIdByPasswordResetToken(token);
			map.put("userId", userId);
			map.put("title", "Reset Password");
			return "resetPassword";
		}
		catch(Exception e){
			System.out.println("Exception catched on initiateResetPassword()");
			map.put("errorMessage","This link is not valid any more");
			return "login";
		}
	}
	
	@RequestMapping(value="/web/resetpassword.htm",method=RequestMethod.POST)
	public String processResetPassword(@RequestParam("userId") Integer userId,@RequestParam("password") String password,@RequestParam("cpassword") String confPassword,Map<String,Object> map){
		try{
			if(password.equals(confPassword)){
				this.userService.handlePasswordReset(userId, password);
				map.put("successMessage","Password Reseted successfully");
				return "login";
			}
			else{
				map.put("errorMessage","Confirmation Password doesn't match Password");
				return "resetPassword";
			}
		}
		catch(Exception e){
			System.out.println("Exception catched on processResetPassword()");
			map.put("errorMessage","cant reset password");
			return "login";
		}
	}
	
}
