package com.log8430.group9.views;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.log8430.group9.commands.Command;

/**
 * This class represents the graphical interface for commands.
 * An instance of the class is a line on the command panel in the main window.
 * Contains the command, the button and the result label. 
 * Also connect the button click with the command's execution.
 */
public class UICommand extends JPanel {
	
	protected Command command;
	protected JButton commandButton;
	protected JLabel commandResult;
	protected FileNode currentFile;
	protected String currentAPI;
	
	/**
	 * Constructor UICommand.
	 * <p>
	 * Create the graphical interface for one command.
	 * </p>
	 * @param command
	 */
	public UICommand(Command command) {
		this.command = command;
		this.commandButton = new JButton(command.getName());
		this.commandButton.setEnabled(false);
		this.commandResult = new JLabel();
		
		this.commandButton.addActionListener(event -> {
			this.execute();
		});
		
		this.setLayout(new GridLayout(1,2));
		this.setMaximumSize(new Dimension(800,50));
		this.add(this.commandButton);
		this.add(this.commandResult);
	}

	/**
	 * Executes the command with the current file selected.
	 * Print the result in the label next to the button.
	 */
	public void execute() {
		try {
			this.commandResult.setText(this.command.execute(this.currentFile.getId(), this.currentAPI));
		} catch(Exception e) {
			this.commandResult.setText(e.getMessage());
		}
		
	}
	
	/**
	 * Clear the result label on the graphical interface.
	 */
	public void clear() {
		this.commandResult.setText("");
	}
	
	/**
	 * Modifies the current file. 
	 * Checks if the command is executable with the file type (file or folder) 
	 * and enables or disables the button consequently.
	 * 
	 * @param file the new current file
	 */
	public void setCurrentFile(FileNode file) {
		this.currentFile = file;
		this.commandButton.setEnabled(this.isEnabled());
		this.clear();
	}
	
	public void setCurrentAPI(String api) {
		this.currentAPI = api;
	}
	
	/**
	 * Returns if the command can be executed giving the current file type (file or folder).
	 * 
	 * @return a boolean telling if the command can be executed giving the current file type (file or folder).
	 */
	public boolean isEnabled() {
		if(this.currentFile == null) {
			return false;
		}
		if(!this.currentFile.isLeaf() && !this.command.folderCompatible()) {
			return false;
		} else if(this.currentFile.isLeaf() && !this.command.fileCompatible()) {
			return false;
		} else {
			return true;
		}
	}
	
}
