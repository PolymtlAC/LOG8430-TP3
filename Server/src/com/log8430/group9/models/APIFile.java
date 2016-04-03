package com.log8430.group9.models;

import java.io.File;
import java.util.ArrayList;

public class APIFile {

	private String id;
    private String name;
    private String path;
    private ArrayList<APIFile> children;
    private boolean directory;
    
    public APIFile(String id, String name, String path, boolean directory) {
    	this.id = id;
    	this.name = name;
    	this.path = path;
    	this.directory = directory;
    	this.children = new ArrayList<>();
    }

    public APIFile(String name) {
    	this("", name, "", false);
    }
    
    public APIFile(File file, int depth) {
        this.name = file.getName();
        this.path = file.getPath().replaceFirst(System.getProperty("user.dir")+"/root/?", "/");
        this.id = this.path;
        this.directory = file.isDirectory();
        if(depth > 0 && this.directory) {
        	this.children = new ArrayList<APIFile>();
	        for(File child : file.listFiles()) {
				this.children.add(new APIFile(child, depth-1));
			}
        } else {
        	this.children = null;
        }
        
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public String getPath() {
		return path;
	}
	
	public ArrayList<APIFile> getChildren() {
		return children;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addChild(APIFile child) {
		this.children.add(child);
	}
}
