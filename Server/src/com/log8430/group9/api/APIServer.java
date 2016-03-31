package com.log8430.group9.api;

public class APIServer implements API {
	
	public String auth() {
		return "test server";
	}

	@Override
	public String getName() {
		return "server";
	}

}
