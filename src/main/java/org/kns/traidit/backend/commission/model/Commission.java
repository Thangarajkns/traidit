/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Id;

import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@MappedSuperclass
public abstract class Commission implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "commission_id")
	@GeneratedValue
	private Integer Id;
	
	@ManyToOne
	@JoinColumn(name = "benificiary_id",nullable = false)
	private TraidItUser benificiary;
	
	@ManyToOne
	@JoinColumn(name = "payer_id",nullable = false)
	private TraidItUser payer;
	
	@ManyToOne
	@JoinColumn(name = "source_payer")
	private TraidItUser sourcePayer;
	
	@Column(name = "commission_amount",nullable = false)
	private BigDecimal commissionAmount;
	
	@Column(name = "commission_of",nullable = false, length = 20)
	private String commissionOf;
	
	@Column(name = "date",nullable = false)
	private Date date;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public TraidItUser getbenificiary() {
		return benificiary;
	}
	public void setbenificiary(TraidItUser benificiary) {
		this.benificiary = benificiary;
	}
	public TraidItUser getpayer() {
		return payer;
	}
	public void setpayer(TraidItUser payer) {
		this.payer = payer;
	}
	public BigDecimal getCommissionAmount() {
		return commissionAmount;
	}
	public TraidItUser getSourcePayer() {
		return sourcePayer;
	}
	public void setSourcePayer(TraidItUser sourcePayer) {
		this.sourcePayer = sourcePayer;
	}
	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
	/**
	 * Returns CommissionOf type variable from the stored string 
	 * 
	 * @return CommissionOf
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	public CommissionOf getCommissionOf() {
		return CommissionOf.valueOf(commissionOf);
	}
	
	/**
	 * Sets string value to commissionOf from given CommissionOf type variable.
	 *  
	 * @param commissionOf
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	public void setCommissionOf(CommissionOf commissionOf) {
		this.commissionOf = commissionOf.toString();
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
