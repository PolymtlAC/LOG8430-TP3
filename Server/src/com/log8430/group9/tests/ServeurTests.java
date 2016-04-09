package com.log8430.group9.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.log8430.group9.api.API;
import com.log8430.group9.api.APIFactory;
import com.log8430.group9.models.APIFile;


public class ServeurTests{
	private APIFactory instance = null;
	@Before
	public void setUp() throws Exception {
		instance =APIFactory.getInstance();

	}

	@Test
	public void testLoadServices() {
		int retour = 3;
		Collection<String> nomsServices = new ArrayList<>();
		nomsServices.add("server");
		nomsServices.add("dropbox");
		nomsServices.add("googledrive");

		ArrayList<String> list = new ArrayList<>();
		Iterator<String>loadedList = instance.getListeNomsAPI();
		while(loadedList.hasNext()){
			list.add(loadedList.next());
		}
		Assert.assertEquals("Le nombre de service est incorrect", retour, list.size());
		Assert.assertTrue(list.containsAll(nomsServices));
	}

	@Test
	public void loadFileFromServer(){
		API api = instance.getAPI("server");
		APIFile rootFile = api.metadata("root");
		String path = "root";
		ArrayList<String> childrenList =new ArrayList<>();
		for(APIFile apiFile : rootFile.getChildren()){
			childrenList.add(apiFile.getName());
		}
		ArrayList<String> expectedChildren = new ArrayList<>();
		expectedChildren.add("code");
		expectedChildren.add("image");
		expectedChildren.add("polymtl");
		expectedChildren.add("article.pdf");

		Assert.assertEquals("le fichier chargé est incorrect", path, rootFile.getPath());
		Assert.assertTrue(expectedChildren.containsAll(childrenList));
	}

}
