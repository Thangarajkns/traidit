/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.backend.refferral.dao;

import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotSavedException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface RefferralDao {
	public RefferralToken saveOrUpdateRefferralToken(RefferralToken token) throws RefferralTokenNotSavedException;
	public RefferralToken getRefferralTokenByIP(String IP) throws RefferralTokenNotFoundException;
//	public TraidItUser getRefferralByIPAndOsVersion(String IP,String osVersion) throws UserNotFoundException;
	public void deleteRefferralToken(RefferralToken tk);
	public Integer getRefferralCountByUserId(Integer userId);
}
