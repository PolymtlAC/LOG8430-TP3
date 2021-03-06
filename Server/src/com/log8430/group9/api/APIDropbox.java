package com.log8430.group9.api;

import java.net.HttpURLConnection;

import org.json.JSONObject;

import com.log8430.group9.models.APIFile;
/**
 * API du service de gestion de fichier Dropbox
 * @author LOG8430 group9
 *
 */
public class APIDropbox extends AbstractAPI implements API {
	/**
	 * clé d'identité du serveur pour la connexion
	 */
	private static String apiKey = "0b5l8skd2z5xujs";
	/**
	 * clé d'authentification du serveur pour la connexion
	 */
	private static String apiSecret = "gha8o37bytj0wae";
	
	@Override
	public String getName() {
		return "dropbox";
	}
	
	@Override
	public void askForToken(String code) {
		this.token = null;
		String result = this.post("https://api.dropboxapi.com/1/oauth2/token", "grant_type=authorization_code"
				+ "&client_id="+apiKey+"&client_secret="+apiSecret
				+ "&redirect_uri=http://localhost:8080/api/code?api=dropbox"
				+ "&code="+code);

		JSONObject json = new JSONObject(result);
		if(json.has("access_token")) {
			this.token = json.getString("access_token");
		}
	}
	
	@Override
	public boolean isConnected() {
		if(this.token == null) {
			return false;
		} else {
			JSONObject json = new JSONObject(this.get("https://api.dropboxapi.com/1/account/info",""));
			if(json.has("display_name")) {
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
	public void addConnectionProperties(HttpURLConnection connection) {
		if(this.token != null) {
			connection.setRequestProperty ("Authorization", "Bearer " + this.token);
		}
	}

	@Override
	public APIFile metadata(String id) {
		String result = this.get("https://api.dropboxapi.com/1/metadata/auto"+id, "list=true");
		JSONObject json = new JSONObject(result);
		String name;
		if(id.equals("/")) {
			name = "/";
		} else {
			String[] tmp = id.split("/");
			name = tmp[tmp.length-1];
		}
		
		APIFile apiFile = new APIFile(id, name, id, json.getBoolean("is_dir"));
		
		if(json.optJSONArray("contents") != null) {
			for(Object obj : json.getJSONArray("contents")) {
				JSONObject jsonChild = (JSONObject) obj;
				String childPath = jsonChild.getString("path");
				String[] tmp2 = childPath.split("/");
				String childName = tmp2[tmp2.length-1];
				
				apiFile.addChild(new APIFile(childPath, childName, childPath, jsonChild.getBoolean("is_dir")));
			}
		}
		
		return apiFile;
	}
}
