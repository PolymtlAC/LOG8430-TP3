package com.log8430.group9.tests;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.log8430.group9.commands.Command;
import com.log8430.group9.commands.CommandLoader;
import com.log8430.group9.views.FileNode;
import com.log8430.group9.views.LazyLoader;
import com.log8430.group9.views.UICommand;

public class ClientTest{
	private LazyLoader fileLoader = null;
	private Command mockCommandFile;
	private Command mockCommandDir;
	private Command mockCommandFD;
	private FileNode fileTest;
	private FileNode dossierTest;
	private CommandLoader commandLoader;
	private ArrayList<UICommand> UICommandsList = new ArrayList<>();
	@Before
	public void setUp() throws Exception {
		//creation des mock compatible fichier, dossier, les deux
		mockCommandFile = EasyMock.createMock(Command.class);
		mockCommandDir = EasyMock.createMock(Command.class);
		mockCommandFD = EasyMock.createMock(Command.class);
		ArrayList<Command> commands = new ArrayList<>();
		commands.add(mockCommandFile);
		commands.add(mockCommandDir);
		commands.add(mockCommandFD);

		//redefinition du Loader pour fournir les mocks
		commandLoader = new CommandLoader(CommandLoader.class.getClassLoader()) {
			@Override
			public ArrayList<Command> loadAllCommands() {
				return commands;
			}
		};
		//creation des elements de tests
		this.fileTest = new FileNode("dossierTest/fichierTest", "fichierTest", "dossierTest/fichierTest", false, "server");
		this.dossierTest = new FileNode("dossierTest", "dossierTest", "dossierTest", true, "server");

	}


	@Test
	public void loadFile(){
		FileNode noeud = LazyLoader.load("root", "server");
		ArrayList<String> childrenList = new ArrayList<>();
		for(FileNode enfant : noeud.getChilden()){
			childrenList.add(enfant.toString());
		}
		ArrayList<String> expectedChildren = new ArrayList<>();
		expectedChildren.add("code");
		expectedChildren.add("image");
		expectedChildren.add("polymtl");
		expectedChildren.add("article.pdf");
		Assert.assertEquals("le fichier chargé est incorrect","root",noeud.getPath());
		Assert.assertTrue("la liste des enfants est incorrecte", childrenList.containsAll(expectedChildren));
	}

	//emulation de la creation de composant graphique des commandes avec la technologie SWING
	public void createMockUICommand(ArrayList<Command> commands){
		for(Command command : commands){
			UICommandsList.add(new UICommand(command));
		}
	}
	@Test
	public void testNumberOfCommands() {
		int retour = 3;
		createMockUICommand(commandLoader.loadAllCommands());
		Assert.assertEquals("La valeur retournee est invalide", retour, UICommandsList.size());
	}
	/**
	 * test si les commandes sont actives ou non ainsi que le fonctionnement de l'execution pour un fichier
	 */
	@Test
	public void testExecutionFOnFile(){
		EasyMock.expect(mockCommandFile.fileCompatible()).andReturn(true);	
		EasyMock.expect(mockCommandFile.getName()).andReturn("commandFile");
		EasyMock.expect(mockCommandFile.execute(fileTest.getId(),fileTest.getAPI())).andReturn("fichierTest");
		EasyMock.expect(mockCommandDir.fileCompatible()).andReturn(false);		 
		EasyMock.expect(mockCommandDir.getName()).andReturn("commandDir");
		EasyMock.expect(mockCommandDir.execute(fileTest.getId(),fileTest.getAPI())).andReturn("dossierTest");
		EasyMock.expect(mockCommandFD.fileCompatible()).andReturn(true);		 
		EasyMock.expect(mockCommandFD.getName()).andReturn("commandFD");
		EasyMock.expect(mockCommandFD.execute(fileTest.getId(),fileTest.getAPI())).andReturn("fichierTest");
		EasyMock.replay(mockCommandFile);
		EasyMock.replay(mockCommandDir);
		EasyMock.replay(mockCommandFD);

		createMockUICommand(commandLoader.loadAllCommands());
		ArrayList<Boolean> returnedValues = new ArrayList<>();
		returnedValues.add(Boolean.TRUE);
		returnedValues.add(Boolean.FALSE);
		returnedValues.add(Boolean.TRUE);
		for(int i = 0; i< UICommandsList.size();i++){

			UICommandsList.get(i).setCurrentFile(fileTest);
			UICommandsList.get(i).setCurrentAPI("server");
			
			Assert.assertEquals("La commande n'a pas le comportement adequat ", returnedValues.get(i), UICommandsList.get(i).getButton().isEnabled());
			UICommandsList.get(i).execute();
			if(UICommandsList.get(i).getButton().isEnabled()){
				Assert.assertEquals("La valeur retourné est fausse", "fichierTest", UICommandsList.get(i).getCommandResult());
			}
		}
	}
	/**
	 * test si les commandes sont actives ou non ainsi que le fonctionnement de l'execution pour un dossier
	 */
	@Test
	public void testExecutionOnFolder(){
		EasyMock.expect(mockCommandFile.folderCompatible()).andReturn(false);		 
		EasyMock.expect(mockCommandFile.getName()).andReturn("commandFile");
		EasyMock.expect(mockCommandFile.execute(dossierTest.getId(),dossierTest.getAPI())).andReturn("fichierTest");
		EasyMock.expect(mockCommandDir.folderCompatible()).andReturn(true);		 
		EasyMock.expect(mockCommandDir.getName()).andReturn("commandDir");
		EasyMock.expect(mockCommandDir.execute(dossierTest.getId(),dossierTest.getAPI())).andReturn("dossierTest");
		EasyMock.expect(mockCommandFD.folderCompatible()).andReturn(true);		 
		EasyMock.expect(mockCommandFD.getName()).andReturn("commandFD");
		EasyMock.expect(mockCommandFD.execute(dossierTest.getId(),dossierTest.getAPI())).andReturn("dossierTest");
		EasyMock.replay(mockCommandFile);
		EasyMock.replay(mockCommandDir);
		EasyMock.replay(mockCommandFD);

		createMockUICommand(commandLoader.loadAllCommands());
		ArrayList<Boolean> returnedValues = new ArrayList<>();
		returnedValues.add(Boolean.FALSE);
		returnedValues.add(Boolean.TRUE);
		returnedValues.add(Boolean.TRUE);

		for(int i = 0; i< 1;i++){
			UICommandsList.get(i).setCurrentFile(dossierTest);
			UICommandsList.get(i).setCurrentAPI("server");
			Assert.assertEquals("La commande n'a pas le comportement adequat ", returnedValues.get(i), UICommandsList.get(i).getButton().isEnabled());
			UICommandsList.get(i).execute();
			if(UICommandsList.get(i).getButton().isEnabled()){
				Assert.assertEquals("La valeur retourn� est fausse", "dossierTest", UICommandsList.get(i).getCommandResult());
			}
		}
	}

}
