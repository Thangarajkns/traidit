package org.kns.traidit.frontend.common.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.kns.traidit.frontend.common.exception.InvalidCardException;

public class PayPal {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	private static PaypalCreditCard card = new PaypalCreditCard();
	
	public static void setCardDetails(PaypalCreditCard customerCard){
		card = customerCard;
	}
	
	public static Map<String, String> makePayment(Properties properties, BigDecimal amount) throws IOException, InvalidCardException {
		if(!card.hasValidDetails())
			throw new InvalidCardException();
		 Map<String,String> map = new HashMap<String, String>();
		String url = properties.getProperty("DoDirectPayment.URL");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		StringBuilder str = new StringBuilder();
		str.append("USER="+properties.getProperty("DoDirectPayment.username"));
		str.append("&PWD="+properties.getProperty("DoDirectPayment.password"));
		str.append("&SIGNATURE="+properties.getProperty("DoDirectPayment.signature"));
		str.append("&METHOD=DoDirectPayment");
		str.append("&VERSION=86");
		str.append("&PAYMENTACTION=Sale");
		str.append("&AMT="+amount);
		str.append("&ACCT="+card.getCardNo());
		str.append("&CREDITCARDTYPE="+card.getCardType());
		str.append("&CVV2="+card.getCardCvv2());
		str.append("&FIRSTNAME="+card.getCardHolderFirstName());
		str.append("&LASTNAME="+card.getCardHolderLastName());
		str.append("&STREET="+card.getCardHolderStreet());
		str.append("&CITY="+card.getCardHolderCity());
		str.append("&STATE="+card.getCardHolderState());
		str.append("&ZIP="+card.getCardHolderZip());
		str.append("&COUNTRYCODE="+properties.getProperty("DoDirectPayment.contrycode"));
		str.append("&CURRENCYCODE="+properties.getProperty("DoDirectPayment.currencycode"));
		str.append("&EXPDATE="+card.getCardExpDate());
		String urlParameters = str.toString();

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println("Response : " + response.toString());
		 for(String data : response.toString().split("&")){
			 String keyValue[] = data.split("=");
			 map.put(keyValue[0], URLDecoder.decode(keyValue[1],"UTF-8"));
		 }

		 return map;
 
	}
	
	
}
