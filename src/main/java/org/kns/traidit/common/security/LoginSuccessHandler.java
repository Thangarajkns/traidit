package org.kns.traidit.common.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.runtime.directive.Foreach;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{


	@Resource(name="userService")
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
			throws ServletException,IOException{
		System.out.println("inside LoginSuccessHandler->onAuthenticationSuccess()");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String user = authentication.getName();
		TraidItUser traiditUser;
		HttpSession httpSession = req.getSession();
		try{
			traiditUser = this.userService.getBasicUserDetails(user);
			httpSession.setAttribute("user", traiditUser);
		}
		catch(Exception e){
			System.out.println("Exception on getBasicUserDetails() onAuthenticationSuccess and cannot set user session attribute");
		}
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		ArrayList<String> roles = new ArrayList<String>();
		for(GrantedAuthority auths:authorities){
			roles.add(auths.getAuthority());
		}
		httpSession.setAttribute("roles", roles);
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(req, res);
		if(savedRequest != null){
			res.sendRedirect(savedRequest.getRedirectUrl());
		}
		else{
			res.sendRedirect(req.getContextPath()+"/web/index.htm");
		}
	}
}
