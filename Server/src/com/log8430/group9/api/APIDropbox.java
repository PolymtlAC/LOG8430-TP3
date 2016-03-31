package com.log8430.group9.api;

public class APIDropbox implements API {
	
	private String token;

	public String auth() {
		return "test";
	}
	
	public String apiKey() {
		return "0b5l8skd2z5xujs";
	}

	@Override
	public String getName() {
		return "dropbox";
	}

	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
