package com.log8430.group9.commands;

/**
 * Common interface that all the commands must implements.
 */
public interface Command {
	
	public static String urlAPI = "http://localhost:8080";
	
	/**
	 * Returns a String which contains the result of the command applied to the file.
	 *
	 * @param id  the identificator of the file
	 * @param api name of the file manager api
	 * @return  a String representing the result of the command
	 */
	public String execute(String id, String api);
	
	/**
	 * Returns true if the command accepts a file as parameter.
	 *
	 * @return  the compatibility with a file parameter
	 */
	public boolean fileCompatible();
	
	/**
	 * Returns true if the command accepts a folder as parameter.
	 *
	 * @return  the compatibility with a folder parameter
	 */
	public boolean folderCompatible();
	
	/**
	 * The name is used in the button label of the command.
	 *
	 * @return  the name of the command
	 */
	public String getName();
	
}
