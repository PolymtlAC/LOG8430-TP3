package com.log8430.group9.views;
import java.io.File;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

/**
 * This class is used to have a good display in the JTree.
 *
 */
public class UIFile implements TreeNode {
	
	protected File file;

	public UIFile(File file) {
		this.file = file;
	}
	
	public UIFile(String pathname) {
		this.file = new File(pathname);
	}
	
	public UIFile(File parent, String child) {
		this.file = new File(parent, child);
	}

	/**
	 * Replace the default File.toString() method which returns the absolute path.
	 * 
	 * @return the file/folder name.
	 */
	@Override
	public String toString() {
		return this.file.getName();
	}
	
	/**
	 * Returns the file which can be retrieve when the selection event is emitted in the JTree view.
	 * 
	 * @return the file/folder.
	 */
	public File getFile() {
		return this.file;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
	    String[] directoryMembers = this.file.list();
	    return (new UIFile(this.file, directoryMembers[childIndex]));
	}

	@Override
	public int getChildCount() {
		if(this.file.isDirectory()) {
			String[] directoryMembers = this.file.list();
			return directoryMembers.length;
	    } else {
	    	return 0;
	    }
	}


	@Override
	public int getIndex(TreeNode node) {
		String[] directoryMembers = this.file.getParentFile().list();
		for(int i=0; i<directoryMembers.length; i++) {
			if(directoryMembers[i] == this.file.getAbsolutePath()) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return this.file.isFile();
	}

	@Override
	public Enumeration children() {
		return null;
	}

	@Override
	public TreeNode getParent() {
		if(this.file.getParentFile() != null) {
			return new UIFile(this.file.getParentFile().getAbsolutePath());
		} else {
			return null;
		}
	}

}
