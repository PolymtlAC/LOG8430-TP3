package com.log8430.group9.api;

import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.log8430.group9.models.APIFile;
/**
 * API du service de gestion de fichier GoogleDrive
 * @author LOG8430 group9
 *
 */
public class APIGoogleDrive extends AbstractAPI implements API {
	/**
	 * clé d'identité du serveur pour la connexion
	 */
	private static String apiKey = "21850492540-vrduvcqo1l2airu6u1d02eivddcip48u.apps.googleusercontent.com";
	/**
	 * clé d'authentification du serveur pour la connexion
	 */
	private static String apiSecret = "9krlDxPFGP4pROuTmZi-Otfl";
	
	@Override
	public String getName() {
		return "googledrive";
	}

	@Override
	public boolean isConnected() {
		if(this.token == null) {
			return false;
		} else {
			JSONObject json = new JSONObject(this.get("https://www.googleapis.com/drive/v2/files/root/children",""));
			if(json.has("items")) {
				return true;
			} else {
				return false;
			}
		}
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
	public APIFile metadata(String id) {
		String result;
		JSONObject json;
		boolean isDir;
		APIFile apiFile;
		
		if(id.equals("/")) {
			apiFile = new APIFile("root", "/", "/", true);
			result = this.get("https://www.googleapis.com/drive/v2/files/root/children", "");
		} else {
			result = this.get("https://www.googleapis.com/drive/v2/files/"+id, "");
			json = new JSONObject(result);
			
			isDir = false;
			if(json.optString("mimeType") == "application/vnd.google-apps.folder")
				isDir = true;
			
			apiFile = new APIFile(id, json.getString("title"), "Not supported in Google Drive", isDir);
			
			// Get the children
			result = this.get("https://www.googleapis.com/drive/v2/files/"+id+"/children", "");
		}
		
		json = new JSONObject(result);
		
		if(json.optJSONArray("items") != null) {
			for(Object obj : json.getJSONArray("items")) {
				JSONObject jsonChild = (JSONObject) obj;
				String childId = jsonChild.getString("id");
				
				result = this.get("https://www.googleapis.com/drive/v2/files/"+childId, "");
				jsonChild = new JSONObject(result);
				
				isDir = false;
				if(jsonChild.optString("mimeType").equals("application/vnd.google-apps.folder"))
					isDir = true;
				
				apiFile.addChild(new APIFile(childId, jsonChild.getString("title"), "", isDir));
			}
		}
	
		return apiFile;
	}
	
}
