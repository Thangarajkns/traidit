/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Entity
@Table(name = "kns_traidit_commission_of_active_account")
public class CommissionOfActiveAccount extends Commission {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		this.setDate(new Date());
    }
}
