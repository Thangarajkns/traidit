/**
 * @since 26-Dec-2014
 */
package org.kns.traidit.frontend.commission.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.kns.traidit.frontend.common.utility.Paginator;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class UserCommissionsFormBean {
	private Paginator paginator = new Paginator();
	private String sortBy = null;
	private String sortOrder = null;
	private ArrayList<CommissionDto> commissions;
	private BigDecimal totalRevenue;
	private BigDecimal commissionSpent;
	private BigDecimal actualRevenue;
	
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
	public ArrayList<CommissionDto> getCommissions() {
		return commissions;
	}
	public void setCommissions(ArrayList<CommissionDto> commissions) {
		this.commissions = commissions;
	}
	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public BigDecimal getCommissionSpent() {
		return commissionSpent;
	}
	public void setCommissionSpent(BigDecimal commissionSpent) {
		this.commissionSpent = commissionSpent;
	}
	public BigDecimal getActualRevenue() {
		return actualRevenue;
	}
	public void setActualRevenue(BigDecimal actualRevenue) {
		this.actualRevenue = actualRevenue;
	}
	
	
}
