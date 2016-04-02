package com.log8430.group9.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.log8430.group9.models.APIFile;

public abstract class AbstractAPI implements API {

	protected String token = null;
	
	public abstract void addConnectionProperties(HttpURLConnection connection);
	
	public String get(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(targetURL + "?" + urlParameters);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			
			this.addConnectionProperties(connection);
			
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
	
	public String post(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;  
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			this.addConnectionProperties(connection);
			connection.setDoOutput(true);
			
			//Send request
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
	
	public String request(HttpURLConnection connection) {
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
