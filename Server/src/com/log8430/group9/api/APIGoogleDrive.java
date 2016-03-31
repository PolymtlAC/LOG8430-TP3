package com.log8430.group9.api;

public class APIGoogleDrive implements API {

	public String auth() {
		return "test";
	}

	@Override
	public String getName() {
		return "googledrive";
	}
	
}
