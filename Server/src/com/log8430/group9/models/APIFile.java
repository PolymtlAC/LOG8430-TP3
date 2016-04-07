package com.log8430.group9.models;

import java.io.File;
import java.util.ArrayList;
/**
 * Structure de donnée definissant un fichier
 * @author LOG8430 group9
 *
 */
public class APIFile {
	/**
	 * identificateur du fichier
	 */
	private String id;
	/**
	 * nom du fichier
	 */
    private String name;
    /**
     * chemin d'acces au fichier
     */
    private String path;
    /**
     * liste des fichiers enfant du fichier courant
     */
    private ArrayList<APIFile> children;
    /**
     * boolean spécifiant si le fichier est un dossier
     */
    private boolean directory;
    /**
     * constructeur de la strucuture
     * @param id : identificateur du fichier
     * @param name nom du fichier
     * @param path chemin d'acces
     * @param directory vrai si le fichier est un dossier
     */
    public APIFile(String id, String name, String path, boolean directory) {
    	this.id = id;
    	this.name = name;
    	this.path = path;
    	this.directory = directory;
    	this.children = new ArrayList<>();
    }
    /**
     * constructeur ne sepcifiant que le nom du fichier
     * @param name nom du fichier
     */
    public APIFile(String name) {
    	this("", name, "", false);
    }
    /**
     * constructeur permettant de construire la structure a partir d'un fichier et d'un profondeur pour la recherche de sous-dossier/fichier
     * @param file : fichier pour construire la structure
     * @param depth profondeur de recherche pour les sous-dossiers/fichiers du fichier courant
     */
    public APIFile(File file, int depth) {
        this.name = file.getName();
        String systemDir = System.getProperty("user.dir");
        systemDir = systemDir.replace("\\", "\\\\");
        this.path = file.getPath().replaceFirst(systemDir+"/root/?", "/");
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
    /**
     * accesseur en lecture de l'identificateur
     * @return l'identificateur du fichier
     */
    public String getId() {
        return id;
    }
    /**
     * accesseur en lecture du nom du fichier
     * @return nom du fichier
     */
    public String getName() {
        return name;
    }
    /**
     * accesseur en lecture du chemin d'acces
     * @return chemin d'acces du fichier
     */
	public String getPath() {
		return path;
	}
    /**
     * accesseur en lecture de la liste des sous-dossiers/fichiers
     * @return liste des sous-dossiers/fichiers du fichier
     */
	public ArrayList<APIFile> getChildren() {
		return children;
	}
	/**
     * accesseur du parametre directory
     * @return vrai si le fichier est un dossier
     */
	public boolean isDirectory() {
		return directory;
	}
	/**
     * accesseur en ecriture du nom de fichier
	 * @param name nom du fichier
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * fonction d'ajout a la liste des sous-dossiers/fichiers
	 * @param child fichier a ajouter a la liste des sous-dossiers/fichiers du fichier courant
	 */
	public void addChild(APIFile child) {
		this.children.add(child);
	}
}
