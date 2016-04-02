package com.log8430.group9.api;

import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.log8430.group9.models.APIFile;

public class APIGoogleDrive extends AbstractAPI implements API {

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
		return this.token != null;
	}

	@Override
	public String getAutorisationCode() {
		return this.code;
	}

	@Override
	public void askForToken(String code) {
		this.token = null;
		/*String result = this.post("https://api.dropboxapi.com/1/oauth2/token", "grant_type=authorization_code"
				+ "&client_id="+apiKey+"&client_secret="+apiSecret
				+ "&redirect_uri=http://localhost:8080/dropbox/code"
				+ "&code="+code);
		
		JSONObject json = new JSONObject(result);
		this.token = json.getString("access_token");*/
	}

	@Override
	public void addConnectionProperties(HttpURLConnection connection) {
		if(this.token != null) {
			connection.setRequestProperty ("Authorization", "Bearer " + this.token);
		}
	}
	
}
