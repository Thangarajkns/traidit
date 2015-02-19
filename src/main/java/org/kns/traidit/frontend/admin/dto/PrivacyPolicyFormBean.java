/**
 * @since 10-Feb-2015
 */
package org.kns.traidit.frontend.admin.dto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class PrivacyPolicyFormBean {

	private String PrivacyPolicy;

	private String submit;

	public String getPrivacyPolicy() {
		return PrivacyPolicy;
	}

	public void setPrivacyPolicy(String privacyPolicy) {
		PrivacyPolicy = privacyPolicy;
	}
	
	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

}
