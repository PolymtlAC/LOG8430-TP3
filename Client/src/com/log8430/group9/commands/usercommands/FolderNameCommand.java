package com.log8430.group9.commands.usercommands;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.log8430.group9.commands.Command;
import com.log8430.group9.utils.Http;

public class FolderNameCommand implements Command {

	@Override
	public String execute(String id, String api) {
		JSONObject json;
		try {
			json = new JSONObject(Http.get(urlAPI+"/command/metadata", "api="+api+"&id="+URLEncoder.encode(id,"UTF-8")));
			return json.getString("name");
		} catch (JSONException | UnsupportedEncodingException e) {
			return "Command Failed";
		}
	}

	@Override
	public boolean fileCompatible() {
		return false;
	}

	@Override
	public boolean folderCompatible() {
		return true;
	}

	@Override
	public String getName() {
		return "Folder Name";
	}

}
