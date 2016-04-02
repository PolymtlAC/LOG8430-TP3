package com.log8430.group9.api;

import com.log8430.group9.APIFile;

public class APIGoogleDrive implements API {

	@Override
	public String getName() {
		return "googledrive";
	}

	@Override
	public APIFile metadata(String path) {
		return null;
	}

	@Override
	public boolean isConnected() {
		return false;
	}
	
}
