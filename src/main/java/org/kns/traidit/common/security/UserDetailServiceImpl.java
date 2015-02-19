package org.kns.traidit.common.security;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Resource;

import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.user.dto.RolesDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService{
	
	@Resource(name="userService")
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		System.out.println("UserDetailServiceImpl -> loadUserByUsername");
		UserDto userDetails = null;
		try{
			userDetails = this.userService.getUserbyUserName(userName);
		}
		catch(Exception e){
			System.out.println("Failed to get User by Username "+e.toString());
		}
		if(userDetails == null||userDetails.getRoles().getRoleId() != 1){
			throw new UsernameNotFoundException("user not found");
		}
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl(userDetails.getRoles().getRoleName()));
		Boolean enabled =true;
		Boolean accountNonExpired =true;
		Boolean credentialsNonExpired =true;
		Boolean accountNonLocked =true;
		User user = new User(userName, userDetails.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		return (UserDetails)user;
	}

}
