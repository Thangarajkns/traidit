/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Entity
@Table(name = "kns_traidit_commission_payment")
public class CommissionPayment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "payment_id")
	private Integer Id;
	
	@ManyToOne
	@JoinColumn(name = "benificiary_id",nullable = false)
	private TraidItUser benificiary;
	
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "payment_type",nullable = false, length = 20)
	private String paymentType;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public TraidItUser getBenificiary() {
		return benificiary;
	}

	public void setBenificiary(TraidItUser benificiary) {
		this.benificiary = benificiary;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CommissionPaymentType getPaymentType() {
		return CommissionPaymentType.valueOf(paymentType);
	}

	public void setPaymentType(CommissionPaymentType paymentType) {
		this.paymentType = paymentType.toString();
	}
	
	/**
	 * 
	 * (@PrePersist)Executed before the entity manager persist operation is actually executed or cascaded. This call is synchronous with the persist operation.
	 * 
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 10-Dec-2014
	 */
	@PrePersist
    public void onCreate() {
		date = new Date();
    }
	
}
