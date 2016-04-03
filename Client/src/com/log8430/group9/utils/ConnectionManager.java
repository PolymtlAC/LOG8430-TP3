package com.log8430.group9.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.json.JSONObject;

public class ConnectionManager {
	
	public static String apiURL = "http://localhost:8080";
	
	public static boolean connect(String api) {
		JSONObject json = new JSONObject(Http.get(apiURL+"/api/is_connected", "api="+api));
		boolean isConnected = json.getBoolean("connected");
		if(isConnected) {
			return true;
		} else {
			return getTokenFromFile(api);
		}
	}
	
	private static boolean getTokenFromFile(String api) {
		boolean isConnected = false;
		
		try(BufferedReader br = new BufferedReader(new FileReader("auth/"+api))) {
		    String token = br.readLine();
		    JSONObject json = new JSONObject(Http.get(apiURL+"/api/auth", "api="+api+"&token="+token));
		    isConnected = json.getBoolean("connected");
		} catch (IOException e) {}
			
		if(isConnected) {
			return true;
		} else {
			return askForAutorization(api);
		}
	}

	private static boolean askForAutorization(String api) {
		switch(api) {
			case "dropbox":
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://www.dropbox.com/1/oauth2/authorize"
								+ "?client_id=0b5l8skd2z5xujs"
								+ "&response_type=code"
								+ "&redirect_uri="+apiURL+"/api/code?api=dropbox"));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
				}
				break;
			case "googledrive":
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://accounts.google.com/o/oauth2/v2/auth"
								+ "?client_id=21850492540-vrduvcqo1l2airu6u1d02eivddcip48u.apps.googleusercontent.com"
								+ "&response_type=code"
								+ "&scope="+URLEncoder.encode("https://www.googleapis.com/auth/drive", "UTF-8")
								+ "&redirect_uri="+URLEncoder.encode(apiURL+"/api/code?api=googledrive", "UTF-8")));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
				}
				break;
		}
		
		boolean isConnected = false;
		int i = 0;
		String token = null;
		while(!isConnected && i < 60) { // while not authorized or wait 30 seconds
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			JSONObject json = new JSONObject(Http.get(apiURL+"/api/is_connected", "api="+api));
			isConnected = json.getBoolean("connected");
			token = json.getString("token");
			i++;
		}
		
		if(isConnected && token != null) {
			saveToken(token, api);
			return true;
		} else {
			return false;
		}
	}

	private static void saveToken(String token, String api) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("auth/"+api, "UTF-8");
			writer.println(token);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
