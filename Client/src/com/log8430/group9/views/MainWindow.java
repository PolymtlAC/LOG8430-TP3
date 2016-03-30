package com.log8430.group9.views;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.log8430.group9.commands.Command;
import com.log8430.group9.commands.CommandLoader;


/**
 * Main window of the application.
 *
 */
public class MainWindow extends JFrame implements Observer, ActionListener, TreeSelectionListener {
	
	protected DefaultTreeModel fileSystemModel;
	protected JTree tree;
	protected JPanel commandPanel;
	protected JButton folderSelectionButton;
	protected JCheckBox autoRunCheckBox;
	protected JButton clearButton;
	protected JFileChooser fileChooser;
	protected ArrayList<UICommand> commands;
	protected File currentFile;
	
	 /**
     * Constructor MainWindow.
     * <p>
     * Construct all the graphical interface and add listeners for buttons.
     * </p>
     */
	public MainWindow() {
		
		this.commands = new ArrayList<>();
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		this.currentFile = new File(System.getProperty("user.home"));
		this.fileSystemModel = new DefaultTreeModel(new UIFile(this.currentFile));
				
		this.setTitle("LOG8430 - Option 1");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		//this.fileSystemModel.setRoot(new File(System.getProperty("user.home")));
		this.tree = new JTree(this.fileSystemModel);
		this.tree.addTreeSelectionListener(this);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		JScrollPane treeView = new JScrollPane(this.tree);
		this.folderSelectionButton = new JButton("Select a file or a folder");
		this.folderSelectionButton.addActionListener(this);
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
		westPanel.add(treeView);
		westPanel.add(this.folderSelectionButton);
		
		this.commandPanel = new JPanel();
		this.commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.PAGE_AXIS));
		
		/*CommandWatcher commandWatcher = new CommandWatcher();
		commandWatcher.addObserver(this);
		Thread thread = new Thread(commandWatcher);
		thread.start();*/
		
		this.clearButton = new JButton("Clear");
		this.clearButton.addActionListener(this);
		this.autoRunCheckBox = new JCheckBox("AutoRun");
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.LINE_AXIS));
		optionsPanel.add(this.clearButton);
		optionsPanel.add(this.autoRunCheckBox);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(this.commandPanel, BorderLayout.CENTER);
		centerPanel.add(optionsPanel, BorderLayout.SOUTH);
		
		JLabel southLabel = new JLabel("Alexandre CHENIEUX - Thomas NEYRAUT - Alexandre PEREIRA");
		southLabel.setHorizontalAlignment(JLabel.CENTER);
		
		this.getContentPane().add(westPanel, BorderLayout.WEST);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(southLabel, BorderLayout.SOUTH);
		
		this.loadCommands();
	}
	
	/**
	 * Adds the commands to the command list.
	 */
	public void loadCommands() {
		this.commandPanel.removeAll();
		this.commands = new ArrayList<>();
		
		CommandLoader commandLoader = new CommandLoader(CommandLoader.class.getClassLoader());
		
		for(Command command : commandLoader.loadAllCommands()) {
			UICommand uiCommand = new UICommand(command);
			this.commandPanel.add(uiCommand);
			this.commands.add(uiCommand);
		}
		
		this.commandPanel.revalidate();
		this.commandPanel.repaint();
		
		// to re-initialise correctly all commands and potentially auto-run them
		this.setCurrentFile(this.currentFile);
	}

	/**
	 * Calls the appropriate method depending of the event source.
	 * 
	 * @param event 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == this.folderSelectionButton) {
			this.selectFolder();
		} else if(event.getSource() == this.clearButton) {
			this.clear();
		}
	}
	
	/**
	 * Called when the user select a file or folder on the tree view.
	 * Replaces the current file by the new file selected.
	 * 
	 * @param event
	 */
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		UIFile uiFile = (UIFile) tree.getLastSelectedPathComponent();
		
		if(uiFile == null)
			return;
		
		this.setCurrentFile(uiFile.getFile());
	}
	
	/**
	 * Update the current file for all commands. 
	 * And if the autoRun is checked, executes all the commands.
	 * 
	 * @param file
	 */
	private void setCurrentFile(File file) {
		this.currentFile = file;
		
        for(UICommand command : commands) {
			command.setCurrentFile(file);
			
			if(this.autoRunCheckBox.isSelected() && command.isEnabled()) {
				command.execute();
			}
		}
	}

	/**
	 * Clears the label of all commands.
	 */
	private void clear() {
		for(UICommand command : commands) {
			command.clear();
		}
	}

	/** 
	 * Opens the file chooser and modifies the current file if the user doesn't cancel.
	 * Update the tree view with the new root if necessary.
	 * If a file is selected the parent directory is set as root in the tree view.
	 */
	public void selectFolder() {
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            if(file.isDirectory()) {
            	this.fileSystemModel.setRoot(new UIFile(file.getAbsolutePath()));
            } else {
            	this.fileSystemModel.setRoot(new UIFile(file.getParentFile().getAbsolutePath()));
            }
            
            this.setCurrentFile(file);
        }
	}

	/**
	 * called when the watcher see changes in the commands files.
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.loadCommands();
	}
	
}
