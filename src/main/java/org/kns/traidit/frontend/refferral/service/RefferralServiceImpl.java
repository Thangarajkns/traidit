/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.frontend.refferral.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.kns.traidit.backend.refferral.dao.RefferralDao;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotSavedException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Transactional
@Service("refferralService")
public class RefferralServiceImpl implements RefferralService{
	
	@Resource(name="refferralDao")
	private RefferralDao refferralDao;
	
	@Resource(name="userService")
	private UserService userService; 
	
	/**
	 *
	 * @param token
	 * @throws RefferralTokenNotSavedException
	 * @return RefferralToken
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	public RefferralToken saveOrUpdateRefferralToken(RefferralToken token) throws RefferralTokenNotSavedException{
		token = this.refferralDao.saveOrUpdateRefferralToken(token);
		return token;
	}

	/**
	 *
	 * @param IP
	 * @throws RefferralTokenNotFoundException
	 * @return RefferralToken
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	public RefferralToken getRefferralTokenByIP(String IP) throws RefferralTokenNotFoundException{
		return this.refferralDao.getRefferralTokenByIP(IP);
	}

	/**
	 * 
	 */
	/*public Boolean mapRefferralToUser(Integer userId,HttpServletRequest request) throws UserNotFoundException, RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException{
		String userAgent = request.getHeader("user-agent");
		String osVersion;
		if (userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*"))
		{
			osVersion = userAgent.substring(userAgent.indexOf(" OS ")+4).split(" ",2)[0].replace('_', '.').trim();
			System.out.println(osVersion);
		}
		else
		{
			return false;
		}
		String IP = request.getRemoteAddr();
		UserDto user = this.userService.getUserbyUserId(userId);
		TraidItUser referral = this.refferralDao.getRefferralByIPAndOsVersion(IP,osVersion);
		UserDto refferralDto = new UserDto();
		refferralDto.setUserId(referral.getUserId());
		user.setReferral(refferralDto);
		this.userService.saveOrUpdateTraiditUser(user);
		return true;
	}*/
	
	/**
	 * 
	 * @param userId
	 * @param refferralId
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @throws UserNotFoundException 
	 * @throws RoleNotFoundException 
	 * @throws UserNotSavedOrUpdatedException 
	 * @throws PlanNotFoundException 
	 * @since 02-Feb-2015
	 */
	public Boolean updateRefferral(Integer userId,Integer refferralId) throws UserNotFoundException, RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException{
		try {
			this.userService.updateRefferral(userId, refferralId);
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException();
		} catch (RoleNotFoundException e) {
			throw new RoleNotFoundException();
		} catch (UserNotSavedOrUpdatedException e) {
			throw new UserNotSavedOrUpdatedException();
		} catch (PlanNotFoundException e) {
			throw new PlanNotFoundException();
		}
		return true;
	}
	
	/**
	 * 
	 */
	public void deleteRefferralToken(RefferralToken tk){
		this.refferralDao.deleteRefferralToken(tk);
	}

	/**
	 * 
	 */
	public Integer getRefferralCountByUserId(Integer userId){
		return this.refferralDao.getRefferralCountByUserId(userId);
	}
}
