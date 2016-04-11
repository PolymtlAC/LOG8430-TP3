package com.log8430.group9.views;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONObject;

import com.log8430.group9.commands.Command;
import com.log8430.group9.commands.CommandLoader;
import com.log8430.group9.commands.CommandWatcher;
import com.log8430.group9.utils.ConnectionManager;
import com.log8430.group9.utils.Http;


/**
 * Main window of the application.
 *@author LOG8430 group9
 */
public class MainWindow extends JFrame implements Observer, ActionListener, TreeSelectionListener {

	protected DefaultTreeModel fileSystemModel;
	protected JTree tree;

	protected JPanel commandPanel;
	protected JButton apiSelectionButton;
	protected JCheckBox autoRunCheckBox;
	protected JButton clearButton;

	protected ArrayList<UICommand> commands;
	protected FileNode currentFile;
	protected String currentAPI;

	/**
	 * Constructor MainWindow.
	 * <p>
	 * Construct all the graphical interface and add listeners for buttons.
	 * </p>
	 */
	public MainWindow() {

		this.commands = new ArrayList<>();

		this.currentAPI = "server";
		this.currentFile = null;
		this.fileSystemModel = new DefaultTreeModel(LazyLoader.load("/", this.currentAPI));

		this.setTitle("LOG8430 - groupe 09 - Option 1");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//this.fileSystemModel.setRoot(new File(System.getProperty("user.home")));
		this.tree = new JTree(this.fileSystemModel);
		this.tree.addTreeSelectionListener(this);
		LazyLoader loader = new LazyLoader();
		tree.addTreeWillExpandListener(loader);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		JScrollPane treeView = new JScrollPane(this.tree);
		this.apiSelectionButton = new JButton("Select an API");
		this.apiSelectionButton.addActionListener(this);
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
		westPanel.add(treeView);
		westPanel.add(this.apiSelectionButton);

		this.commandPanel = new JPanel();
		this.commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.PAGE_AXIS));

		CommandWatcher commandWatcher = new CommandWatcher();
		commandWatcher.addObserver(this);
		Thread thread = new Thread(commandWatcher);
		thread.start();

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
		if(event.getSource() == this.apiSelectionButton) {
			this.selectAPI();
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
		FileNode file = (FileNode) tree.getLastSelectedPathComponent();

		if(file == null)
			return;

		this.setCurrentFile(file);
	}

	/**
	 * Update the current file and the current API for all commands. 
	 * And if the autoRun is checked, executes all the commands.
	 * 
	 * @param file
	 */
	private void setCurrentFile(FileNode file) {
		this.currentFile = file;

		for(UICommand command : commands) {
			command.setCurrentFile(file);
			command.setCurrentAPI(this.currentAPI);

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

	public void selectAPI() {
		Iterator<String> listeService = ServiceNameLoader.loadServiceNameList();
		ArrayList<String> buttons = new ArrayList<>();
		while(listeService.hasNext()){
			buttons.add(listeService.next());
		}
		if(buttons.size() == 0){
			JOptionPane.showMessageDialog(null, "Aucune API n'est disponible", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		else{
			int returnValue = JOptionPane.showOptionDialog(null, "Select the API you want to use", "Select an API",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons.toArray(), buttons.get(0));
			if(returnValue >= 0)
				this.currentAPI = buttons.get(returnValue);

			if(!ConnectionManager.connect(this.currentAPI)) {
				this.currentAPI = "server";
			}

			for(UICommand command : commands) {
				command.setCurrentAPI(this.currentAPI);
			}

			this.fileSystemModel.setRoot(LazyLoader.load("/", this.currentAPI));
			this.fileSystemModel.reload();
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
