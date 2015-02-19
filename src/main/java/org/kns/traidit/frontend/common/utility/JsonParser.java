package org.kns.traidit.frontend.common.utility;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.org.json.JSONObject;
import twitter4j.org.json.JSONTokener;

/**
 * 
 * Created by Jeevan Mysore - KNS TECHNOLOGIES CORP on Sept 22, 2014
 *
 */
public class JsonParser {

	public static JSONObject getJson(HttpServletRequest request,HttpServletResponse response)
	{
		
		try
		{			
			InputStream stream=request.getInputStream();		
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];			
			for (int n; (n = stream.read(b)) != -1;) 
			{
				buffer.append(new String(b,0,n));
			}
			String s=buffer.toString().trim();
			System.out.println("JSON "+s);
			JSONTokener tokenizer=new JSONTokener(s);
			JSONObject json = new JSONObject(tokenizer);
			return json;			
		}
		catch(Exception e)
		{
			System.out.println("ERROR "+e.toString());
			e.printStackTrace();
			return null;
		}		
	}
}
	
