package org.kns.traidit.frontend.common.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.simple.JSONObject;

public class JSONRequest {
	public static void main(String[] args) throws Exception{

		 //prepare to make Http request 
        String url = "http://localhost:8080/traidit" + "/additem.htm";
        
        //add name value pair for the country code
		JSONObject json = new JSONObject();
		json.put("description","6th sense cooling technology");  
		json.put("itemname","whirlpoola fridge");
		json.put("manufacturer","asdf");
		json.put("photos","asdf.jpg");
		json.put("videos","asdf.swd");
		json.put("categoryid","1");
		json.put("localdbitem","0");
		json.put("userid","2");
//		BufferedImage img = ImageIO.read(new File("D:\\thangaraj\\flat-roof-villa.jpg"));             
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(img, "jpg", baos);
//		baos.flush();
//
//	byte[]   encodedImage = Base64.encodeBase64(baos.toByteArray());
//	System.out.println("ecncoded value is " + new String(encodedImage ));
//
//		baos.close();
//
//		json.put("image",encodedImage);
        String response = sendHttpRequest(url,json);
        System.out.println(response);
	} 


	private static String sendHttpRequest(String url, JSONObject json) {
		int REGISTRATION_TIMEOUT = 15 * 1000;
		int WAIT_TIMEOUT = 6 * 1000;
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		HttpResponse response;
		String content = "";
		try {
		System.out.println("inside try");
		//http request parameters
		HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
		
		//http POST
		HttpPost httpPost = new HttpPost(url);
		StringEntity jsonparams = new StringEntity(json.toString());
		httpPost.setEntity(jsonparams); 

		System.out.println(httpPost.toString());
		//send the request and receive the repponse
		response = httpclient.execute(httpPost);

		System.out.println(response.toString());
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		response.getEntity().writeTo(out);
		out.close();
		content = out.toString();
		}
		
		else{
		//Closes the connection.
		System.out.println(statusLine.getReasonPhrase());
		response.getEntity().getContent().close();
		throw new IOException(statusLine.getReasonPhrase());
		}
		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//send back the JSON response String
		return content;
		
		}
}
