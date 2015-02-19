/**
 * @since 26-Dec-2014
 */
package org.kns.traidit.frontend.commission.dto;

import org.kns.traidit.frontend.common.utility.Paginator;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class CommissionPaymentsFormBean {
	private UserDto user;
	private Paginator paginator = new Paginator();
	private String sortBy = null;
	private String sortOrder = null;
	private String referer = null;
	
	
	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Paginator getPaginator() {
		return paginator;
	}
	
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	
	public String getSortBy() {
		return sortBy;
	}
	
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	public String getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
}
