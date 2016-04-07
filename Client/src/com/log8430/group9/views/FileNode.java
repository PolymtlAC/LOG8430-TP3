package com.log8430.group9.views;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.json.JSONObject;

import com.log8430.group9.utils.Http;

/**
 * This class is used to have a good display in the JTree.
 * @author LOG8430 group9
 *
 */
public class FileNode implements TreeNode {
	/**
	 * identificateur du noeud
	 */
	private String id;
	/**
	 * nom du nom
	 */
	private String name;
	/**
	 * chemin d'acces au fichier du noeud
	 */
	private String path;
	/**
	 * liste des noeuds enfants
	 */
	private ArrayList<FileNode> children;
	/**
	 * boolean specifiant si le noeud represente un dossier
	 */
	private boolean directory;
	/**
	 * nom du service de gestion de fichier
	 */
	private String api;
	
	/**
	 * constructeur du noeud
	 * @param id identificateur du fichier 
	 * @param filename nom du fichier qui sera egalement le nom du noeud
	 * @param path chemin d'acces au fichier representé par le noeud
	 * @param directory definition si le noeud aura des enfants
	 * @param api nom du service de gestion de fichier
	 */
	public FileNode(String id, String filename, String path, boolean directory, String api) {
		this.id = id;
		this.name = filename;
		this.path = path;
		this.directory = directory;
		this.api = api;
		this.children = new ArrayList<>();
	}
	
	
	
	/**
	 * Replace the default File.toString() method which returns the absolute path.
	 * 
	 * @return the file/folder name.
	 */
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
	    return this.children.get(childIndex);
	}

	@Override
	public int getChildCount() {
    	return this.children.size();
	}


	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return !this.directory;
	}

	@Override
	public Enumeration children() {
		return this.children();
	}

	
	@Override
	public TreeNode getParent() {
		return null;
	}
	/**
	 * accesseur en lecture du chemin d'acces au fichier
	 * @return le chemin d'acces au fichier
	 */
	public String getPath() {
		return path;
	}
	/**
	 * fonction d'ajout de noeud enfant au noeud courant
	 * @param child le noeud a ajouter
	 */
	public void addChild(FileNode child) {
		this.children.add(child);
	}
	/**
	 * accesseur en lecture au nom de l'API du service de gestion de fichier
	 * @return le nom de l'API
	 */
	public String getAPI() {
		return this.api;
	}
	/**
	 * accesseur en lecture de la liste des noeuds enfants
	 * @return
	 */
	public ArrayList<FileNode> getChilden() {
		return this.children;
	}
	/**
	 * accesseur en ecriture de la liste des noeuds enfants
	 * @param children liste des noeuds enfants
	 */
	public void setChildren(ArrayList<FileNode> children) {
		this.children = children;
	}
	/**
	 * 	accesseur en lecture de l'identificateur du noeud
	 * @return identificateur du noeud
	 */
	public String getId() {
		return this.id;
	}

}
