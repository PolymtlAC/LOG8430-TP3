package com.log8430.group9.views;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.json.JSONObject;

import com.log8430.group9.utils.ConnectionManager;
import com.log8430.group9.utils.Http;
/**
 * Classe gerant le chargement des fichiers de l'abre du sercice de gestion de fichier lors de l'expansion des noeuds dans l'interface utilisateur
 * @author LOG8430 group9
 *
 */
public class LazyLoader implements TreeWillExpandListener {
	/**
	 * fonction de chargement d'un fichier identifier par son id depuis l'api
	 * @param id identificateur du fichier a chager
	 * @param api nom du service de gestion de fichier où se trouve le fichier demandé
	 * @return
	 */
	public static FileNode load(String id, String api) {
		String params;
		try {
			params = "api="+api+"&id="+URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			params = "api="+api+"&id=/";
		}
		System.out.println(Http.get(ConnectionManager.apiURL+"/command/metadata", params));
		JSONObject json = new JSONObject(Http.get(ConnectionManager.apiURL+"/command/metadata", params));

		FileNode node = new FileNode(json.getString("id"), json.getString("name"), json.getString("path"), json.getBoolean("directory"), api);
		if(json.optJSONArray("children") != null) {
			for(Object child : json.optJSONArray("children")) {
				JSONObject jsonChild = (JSONObject) child;
				node.addChild(new FileNode(jsonChild.getString("id"), jsonChild.getString("name"), jsonChild.getString("path"), jsonChild.getBoolean("directory"), api));
			}
		}
		
		return node;
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath treepath = event.getPath();
		Object[] nodes =  treepath.getPath();
		FileNode lastNode = (FileNode) nodes[nodes.length-1];
	
		FileNode node = LazyLoader.load(lastNode.getId(), lastNode.getAPI());
		lastNode.setChildren(node.getChilden());
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
	}

}
