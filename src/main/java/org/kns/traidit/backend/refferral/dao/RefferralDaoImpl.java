/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.backend.refferral.dao;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotSavedException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.org.json.JSONException;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Repository("refferralDao")
@Transactional
public class RefferralDaoImpl implements RefferralDao {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	/**
	 *
	 * @param token
	 * @throws RefferralTokenNotSavedException
	 * @return RefferralToken
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	public RefferralToken saveOrUpdateRefferralToken(RefferralToken token) throws RefferralTokenNotSavedException{
		sessionFactory.getCurrentSession().saveOrUpdate(token);
		sessionFactory.getCurrentSession().flush();
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(RefferralToken.class);
		criteria.add(Restrictions.eq("userIP", IP));
		ArrayList<RefferralToken> list = (ArrayList<RefferralToken>) criteria.list();
		if(list.isEmpty()){
			throw new RefferralTokenNotFoundException();
		}
		return list.get(0);
	}

	/*public TraidItUser getRefferralByIPAndOsVersion(String IP,String osVersion) throws UserNotFoundException{
		return null;
	}*/
	
	/**
	 * 
	 * @param tk
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 02-Feb-2015
	 */
	public void deleteRefferralToken(RefferralToken tk){
		sessionFactory.getCurrentSession().delete(tk);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * 
	 */
	public Integer getRefferralCountByUserId(Integer userId){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItUser.class);
		criteria.add(Restrictions.eq("referral.userId", userId));
		criteria.setProjection(Projections.count("userId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}
}
