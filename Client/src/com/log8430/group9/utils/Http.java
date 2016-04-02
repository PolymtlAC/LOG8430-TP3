package com.log8430.group9.utils;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Classe helper permettant d'envoyer des requêtes HTTP et recevoir la réponse de manière synchrone
 * Code en partie copié de Stack Overflow : http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
 * et http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
 */
public class Http {
	
	public static String get(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(targetURL + "?" + urlParameters);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			
			return request(connection);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	public static String post(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;  
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			connection.setDoOutput(true);
			
			//Send request
			//urlParameters = URLEncoder.encode(urlParameters,"UTF-8");
			OutputStream wr = connection.getOutputStream();
			wr.write(urlParameters.getBytes("UTF-8"));
			wr.close();
			
			return request(connection);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	public static String request(HttpURLConnection connection) {
		try {
			InputStream is;
			if (connection.getResponseCode() >= 400) {
			    is = connection.getErrorStream();
			} else {
			    is = connection.getInputStream();
			}
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
