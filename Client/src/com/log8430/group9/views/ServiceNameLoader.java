package com.log8430.group9.views;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.log8430.group9.utils.ConnectionManager;
import com.log8430.group9.utils.Http;

public class ServiceNameLoader {
	public static Iterator<String> loadServiceNameList() {
		System.out.println(Http.get(ConnectionManager.apiURL+"/command/listeService",""));
		JSONArray json = new JSONArray(Http.get(ConnectionManager.apiURL+"/command/listeService",""));
		ArrayList<String> liste = new ArrayList<>();		
		for(Object child : json){
			String service = (String)child;
			liste.add(service);
		}
		return liste.iterator();
	}
}
