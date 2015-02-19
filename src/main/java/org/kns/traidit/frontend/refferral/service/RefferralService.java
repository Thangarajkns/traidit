/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.frontend.refferral.service;

import javax.servlet.http.HttpServletRequest;

import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotSavedException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface RefferralService {
	public RefferralToken saveOrUpdateRefferralToken(RefferralToken token) throws RefferralTokenNotSavedException;
	public RefferralToken getRefferralTokenByIP(String IP) throws RefferralTokenNotFoundException;
//	public Boolean mapRefferralToUser(Integer userId,HttpServletRequest request) throws UserNotFoundException, RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException;
	public Boolean updateRefferral(Integer userId,Integer refferralId) throws UserNotFoundException, RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException;
	public void deleteRefferralToken(RefferralToken tk);
	public Integer getRefferralCountByUserId(Integer userId);
}
