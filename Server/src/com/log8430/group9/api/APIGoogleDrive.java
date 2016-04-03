package com.log8430.group9.api;

import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.log8430.group9.models.APIFile;

public class APIGoogleDrive extends AbstractAPI implements API {

	private static String apiKey = "21850492540-vrduvcqo1l2airu6u1d02eivddcip48u.apps.googleusercontent.com";
	private static String apiSecret = "9krlDxPFGP4pROuTmZi-Otfl";
	
	@Override
	public String getName() {
		return "googledrive";
	}

	@Override
	public boolean isConnected() {
		return this.token != null;
	}
	
	@Override
	public void setAccessToken(String token) {
		this.token = token;
	}
	
	@Override
	public String getAccessToken() {
		return this.token;
	}

	@Override
	public void askForToken(String code) {
		this.token = null;
		String result = this.post("https://accounts.google.com/o/oauth2/token", "grant_type=authorization_code"
				+ "&client_id="+apiKey+"&client_secret="+apiSecret
				+ "&redirect_uri=http://localhost:8080/api/code?api=googledrive"
				+ "&code="+code);
		
		JSONObject json = new JSONObject(result);
		if(json.has("access_token")) {
			this.token = json.getString("access_token");
		}
	}

	@Override
	public void addConnectionProperties(HttpURLConnection connection) {
		if(this.token != null) {
			connection.setRequestProperty ("Authorization", "Bearer " + this.token);
		}
	}
	
	@Override
	public APIFile metadata(String path) {
		
		String result = this.get("https://www.googleapis.com/drive/v2/files", "");
		
		JSONObject json = new JSONObject(result);
		
		APIFile apiFile = new APIFile("/", path, true);
		
		if(json.optJSONArray("items") != null) {
			for(Object obj : json.getJSONArray("items")) {
				JSONObject jsonChild = (JSONObject) obj;
				String childName = jsonChild.getString("title");
				
				boolean isDir = false;
				if(json.optString("mimeType") == "application/vnd.google-apps.folder")
					isDir = true;
				
				apiFile.addChild(new APIFile(childName, "/"+childName, isDir));
			}
		}
	
		return apiFile;
	}
	
}
