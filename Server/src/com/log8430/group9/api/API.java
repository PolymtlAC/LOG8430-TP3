package com.log8430.group9.api;

import java.net.HttpURLConnection;

import com.log8430.group9.models.APIFile;

public interface API {
	
	public String getName();
	public boolean isConnected();
	public void setAccessToken(String token);
	public String getAccessToken();
	public void askForToken(String code);
	
	public APIFile metadata(String id);
}
