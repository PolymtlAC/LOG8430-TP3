package com.log8430.group9;

import java.io.File;
import java.util.ArrayList;

public class APIFile {

    private final String name;
    private final String path;
    private final ArrayList<APIFile> children;
    private final boolean directory;

    public APIFile(File file) {
        this.name = file.getName();
        this.path = file.getPath().replaceFirst(System.getProperty("user.dir")+"/root/?", "/");
        this.directory = file.isDirectory();
        this.children = new ArrayList<APIFile>();
    }

    public String getName() {
        return name;
    }

	public String getPath() {
		return path;
	}
	
	public void setChildren(File file) {
		for(File child : file.listFiles()) {
			this.children.add(new APIFile(child));
		}
	}

	public ArrayList<APIFile> getChildren() {
		return children;
	}

	public boolean isDirectory() {
		return directory;
	}
}
