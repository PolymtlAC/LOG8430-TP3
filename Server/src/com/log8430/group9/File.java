package com.log8430.group9;

public class File {

    private final String name;
    private final String path;

    public File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

	public String getPath() {
		return path;
	}
}
