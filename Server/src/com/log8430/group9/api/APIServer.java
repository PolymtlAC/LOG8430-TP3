package com.log8430.group9.api;

import java.io.File;

import com.log8430.group9.APIFile;

public class APIServer implements API {


	@Override
	public String getName() {
		return "server";
	}

	@Override
	public APIFile metadata(String path) {
		String filePath = System.getProperty("user.dir")+"/root";
    	if(!path.equals("/")) {
    		filePath += path;
    	}
    	
    	try {
    		File file = new File(filePath);
        	APIFile apiFile = new APIFile(file, 1);
        	if(path.equals("/")) {
        		apiFile.setName("/");
        	}
        	
        	return apiFile;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new APIFile(filePath);
    	}
	}

	@Override
	public boolean isConnected() {
		return true;
	}

}
