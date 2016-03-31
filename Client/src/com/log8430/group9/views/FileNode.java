package com.log8430.group9.views;
import java.io.File;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

/**
 * This class is used to have a good display in the JTree.
 *
 */
public class FileNode implements TreeNode {
	
	public FileNode() {
	}
	
	/**
	 * Replace the default File.toString() method which returns the absolute path.
	 * 
	 * @return the file/folder name.
	 */
	@Override
	public String toString() {
		return "";
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
	    return new FileNode();
	}

	@Override
	public int getChildCount() {
    	return 0;
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
		return true;
	}

	@Override
	public Enumeration children() {
		return null;
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

}
