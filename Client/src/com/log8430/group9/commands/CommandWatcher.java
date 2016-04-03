package com.log8430.group9.commands;
import java.io.File;
import java.util.HashMap;
import java.util.Observable;

/**
 * Watches the commands folder and when a file is added, removed or changed,
 * tells the window to reload all commands.
 *
 */
public class CommandWatcher extends Observable implements Runnable {
	
	protected HashMap<String,Long> files;
	
	public CommandWatcher() {
		this.init();
	}
	
	/**
	 * Reset the map and save the current last modification date of the commands files.
	 */
	public void init() {
		this.files = new HashMap<>();
		
		File commandsDir = new File("commands");
		if(commandsDir.listFiles() != null) {
			for(File file : commandsDir.listFiles()) {
				this.files.put(file.getName(), file.lastModified());
			}
		}
	}
	
	/**
	 * Watch the folder in another thread. 
	 * Checks the number of files and the last modification date of files.
	 * 
	 * Tells the window to reload commands in case of change.
	 */
	@Override
	public void run() {
	    while(true) {
	    	File commandsDir = new File("commands");
	    	File[] commandsFile = commandsDir.listFiles();
	    	if(commandsFile != null) {
		    	if(commandsFile.length != files.size()) {
		    		this.setChanged();
		    		this.notifyObservers();
		    		this.init();
		    	} else {
		    		for(File file : commandsFile) {
		    			if(this.files.get(file.getName()) != file.lastModified()) {
		    				this.setChanged();
		    				this.notifyObservers();
		    				this.init();
		    				break;
		    			}
		    		}
		    	}
		    	
		    	try {
		    		Thread.sleep(500);
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}
	    	}
	    }
	}
	
}
