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
 *
 */
public class FileNode implements TreeNode {
	
	private String name;
	private String path;
	private ArrayList<FileNode> children;
	private boolean directory;
	private String api;
	
	public FileNode(String filename, String path, boolean directory, String api) {
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

	public String getPath() {
		return path;
	}

	public void addChild(FileNode child) {
		this.children.add(child);
	}

	public String getAPI() {
		return this.api;
	}

	public ArrayList<FileNode> getChilden() {
		return this.children;
	}

	public void setChildren(ArrayList<FileNode> children) {
		this.children = children;
	}

	public String getAbsolutePath() {
		return this.path;
	}

}
