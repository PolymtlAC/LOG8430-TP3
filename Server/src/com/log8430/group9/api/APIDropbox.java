package com.log8430.group9.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.log8430.group9.APIFile;

public class APIDropbox extends AbstractAPI implements API {
	
	private String token = null;
	private static String apiKey = "0b5l8skd2z5xujs";
	
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String getName() {
		return "dropbox";
	}

	@Override
	public APIFile metadata(String path) {
		String result = this.get("https://api.dropboxapi.com/1/metadata/auto"+path, "list=true");
		JSONObject json = new JSONObject(result);
		String name;
		if(path.equals("/")) {
			name = "/";
		} else {
			String[] tmp = path.split("/");
			name = tmp[tmp.length-1];
		}
		
		APIFile apiFile = new APIFile(name, path, json.getBoolean("is_dir"));
		
		if(json.optJSONArray("contents") != null) {
			for(Object obj : json.getJSONArray("contents")) {
				JSONObject jsonChild = (JSONObject) obj;
				String childPath = jsonChild.getString("path");
				String[] tmp2 = childPath.split("/");
				String childName = tmp2[tmp2.length-1];
				
				apiFile.addChild(new APIFile(childName, childPath, jsonChild.getBoolean("is_dir")));
			}
		}
		
		return apiFile;
	}

	@Override
	public void addConnectionProperties(HttpURLConnection connection) {
		if(this.token != null) {
			connection.setRequestProperty ("Authorization", "Bearer " + this.token);
		}
	}

	@Override
	public boolean isConnected() {
		return this.token != null;
	}
}
