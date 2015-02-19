package org.kns.traidit.frontend.common.utility;

public class PaypalCreditCard {

	private String cardHolderFirstName = "";
	private String cardHolderLastName = "";
	private String cardHolderStreet = "";
	private String cardHolderCity = "";
	private String cardHolderState = "";
	private String cardHolderZip = "";
	private String cardType;
	private String cardNo;
	private String cardExpDate;
	private String cardCvv2;
	
	public String getCardHolderFirstName() {
		return cardHolderFirstName;
	}
	public void setCardHolderFirstName(String cardHolderFirstName) {
		this.cardHolderFirstName = cardHolderFirstName;
	}
	public String getCardHolderLastName() {
		return cardHolderLastName;
	}
	public void setCardHolderLastName(String cardHolderLastName) {
		this.cardHolderLastName = cardHolderLastName;
	}
	public String getCardHolderStreet() {
		return cardHolderStreet;
	}
	public void setCardHolderStreet(String cardHolderStreet) {
		this.cardHolderStreet = cardHolderStreet;
	}
	public String getCardHolderCity() {
		return cardHolderCity;
	}
	public void setCardHolderCity(String cardHolderCity) {
		this.cardHolderCity = cardHolderCity;
	}
	public String getCardHolderState() {
		return cardHolderState;
	}
	public void setCardHolderState(String cardHolderState) {
		this.cardHolderState = cardHolderState;
	}
	public String getCardHolderZip() {
		return cardHolderZip;
	}
	public void setCardHolderZip(String cardHolderZip) {
		this.cardHolderZip = cardHolderZip;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardExpDate() {
		return cardExpDate;
	}
	public void setCardExpDate(String cardExpDate) {
		this.cardExpDate = cardExpDate;
	}
	public String getCardCvv2() {
		return cardCvv2;
	}
	public void setCardCvv2(String cardCvv2) {
		this.cardCvv2 = cardCvv2;
	}
	
	public Boolean hasValidDetails(){
		Boolean valid = true;
		if(this.cardType.isEmpty()||this.cardType == null|| !this.cardType.equals("VISA"))
			valid = false;
		else if(this.cardNo.isEmpty()||this.cardNo == null)
			valid = false;
		else if(this.cardExpDate.isEmpty()||this.cardExpDate == null || this.cardExpDate.length()!=6)
			valid = false;
		else if(this.cardCvv2.isEmpty()||this.cardCvv2 == null)
			valid = false;
		return valid;
	}
}
