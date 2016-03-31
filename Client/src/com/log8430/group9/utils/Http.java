package com.log8430.group9.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe helper permettant d'envoyer des requêtes HTTP et recevoir la réponse de manière synchrone
 * Code en partie copié de Stack Overflow : http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
 */
public class Http {
	
	public static String get(String targetURL, String urlParameters) {
		return request(targetURL, urlParameters, "GET");
	}
	
	public static String post(String targetURL, String urlParameters) {
		return request(targetURL, urlParameters, "POST");
	}
	
	public static String request(String targetURL, String urlParameters, String method) {
		HttpURLConnection connection = null;  
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();
			
			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
		    }
		    rd.close();
		    
		    return response.toString();
		    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
