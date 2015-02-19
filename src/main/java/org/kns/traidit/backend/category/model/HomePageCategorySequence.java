/**
 * @since 14-Jan-2015
 */
package org.kns.traidit.backend.category.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Entity
@Table(name="kns_traidit_home_page_category_sequence")
public class HomePageCategorySequence {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="category_id")
	private TraidItItemCategories category;
	
	@Column(name="sequence_no")
	private Integer sequenceNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItItemCategories getCategory() {
		return category;
	}

	public void setCategory(TraidItItemCategories category) {
		this.category = category;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	
}
