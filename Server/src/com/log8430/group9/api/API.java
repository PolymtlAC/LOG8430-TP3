package com.log8430.group9.api;

import java.net.HttpURLConnection;

import com.log8430.group9.APIFile;

public interface API {
	
	public String getName();
	public APIFile metadata(String path);
	public boolean isConnected();
	
}
