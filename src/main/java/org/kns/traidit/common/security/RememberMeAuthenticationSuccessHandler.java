package org.kns.traidit.common.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class RememberMeAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Resource(name="userService")
	private UserService userService;
	
	private Logger log =Logger.getLogger(RememberMeAuthenticationSuccessHandler.class);
	
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws ServletException,IOException{
		log.info("inside RememberMeAuthenticationSuccessHandler");
		System.out.println("inside RememberMeAuthenticationSuccessHandler");
		
		HttpSession session = request.getSession();
		String userName = authentication.getName();
		TraidItUser traiditUser = new TraidItUser();
		try {
			traiditUser = this.userService.getBasicUserDetails(userName);
		} catch (Exception e) {
			log.error("exception while fetching user details in RememberMeAuthenticationSuccessHandler "+e);
			System.out.println("exception while fetching user details in RememberMeAuthenticationSuccessHandler ");
			e.printStackTrace();
		}
		session.setAttribute("user",traiditUser);
	    super.setAlwaysUseDefaultTargetUrl(true);
	    super.setDefaultTargetUrl(request.getRequestURL().toString());
	    super.onAuthenticationSuccess(request, response, authentication);
	}
}
