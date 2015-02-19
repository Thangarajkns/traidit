package org.kns.traidit.frontend.common.utility;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * This program demonstrates a usage of the MultipartUtility class.
 * 
 * @author www.codejava.net
 * 
 */
public class MultipartFileUploader {

	public static void main(String[] args) {
		String charset = "UTF-8";
		File uploadFile1 = new File("F:/images.jpg");
		File uploadFile2 = new File("F:/output.jpg");
		String requestURL = "http://localhost:8080/traidit" + "/addinventory.htm";

		try {
			MultipartUtility multipart = new MultipartUtility(requestURL,
					charset);

			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");

		/*	multipart.addFormField("description","6th sense cooling technology");  
			multipart.addFormField("itemname","whirlpoola fridge");
			multipart.addFormField("manufacturer","asdf");
			multipart.addFormField("photos","asdf.jpg");
			multipart.addFormField("videos","asdf.swd");
			multipart.addFormField("categoryid","1");
			multipart.addFormField("localdbitem","0");
			multipart.addFormField("userid","2");*/

			JSONObject json = new JSONObject();
			json.put("description","6th sense cooling technology 3");  
			json.put("itemname","whirlpoola fridge saq3");
			json.put("manufacturer","asdf");
			/*json.put("photos","asdf.jpg");
			json.put("videos","asdf.swd");*/
			json.put("details","asdf.swd");
			json.put("categoryid","22");
			json.put("subcategoryid","0");
			json.put("subsubcategoryid","0");
			json.put("flagedited","0");
			json.put("flagimageedited","0");
			json.put("flagmanual","1");
			json.put("localdbitem","0");
			json.put("userid","2");
			json.put("upc", "123456");
			json.put("upc", "123456");
			json.put("availablefortrade", "1");
			json.put("unitsavailable", "1");
			
			
			multipart.addJSON(json);
			System.out.println(json.toString());
			multipart.addFilePart("itemphoto", uploadFile1);
			multipart.addFilePart("itemphoto", uploadFile2);

			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:");

			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println(ex);
		}
	}
}