package com.log8430.group9.api;

public class APIFactory {
	
	private static APIDropbox apiDropbox = new APIDropbox();
	private static APIGoogleDrive apiGoogleDrive = new APIGoogleDrive();
	private static APIServer apiServer = new APIServer();
	
	public static API getAPI(String apiName) {
		switch(apiName) {
			case "dropbox":
				return apiDropbox;
			case "googledrive":
				return apiGoogleDrive;
			default:
				return apiServer;
		}
	}
}
