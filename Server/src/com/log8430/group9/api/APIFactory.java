package com.log8430.group9.api;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/**
 * Liste l'ensemble des APIs des services de gestion de fichier gerer par le serveur
 * @author Alexandre
 *
 */
public class APIFactory{
	
	public static APIFactory instance = null;
	/**
	 * liste des APIs accessibles par la systeme
	 */
	private HashMap<String, API> listeAPI = new HashMap<>();

	/**
	 * retourne l'instance de l'API relative au service de gestion de fichier demandé
	 * @param apiName : nom du service de fichier demandé
	 * @return l'API du service de gestion de fichier
	 */
	private APIFactory(){
		this.loadServices();
		}
	/**
	 * 
	 * @param apiName
	 * @return
	 */
	public API getAPI(String apiName) {
		API api  = listeAPI.get(apiName);
		if(api == null){
			api = listeAPI.get("server");
		}
		return api;
		
	}
	/**
	 * 
	 * @return
	 */
	public static APIFactory getInstance(){
		if(instance == null){
			instance = new APIFactory();
		}
		return instance;
	}
	/**
	 * 
	 * @param api
	 * @param nomAPI
	 */
	public void addAPI(API api, String nomAPI){
		listeAPI.put(nomAPI, api);
	}

	public Iterator<String> getListeNomsAPI(){
		return listeAPI.keySet().iterator();
	}

	private void loadServices() {
		listeAPI.clear();
		ServiceLoader serviceLoader = new ServiceLoader(ServiceLoader.class.getClassLoader());
		for(API service :serviceLoader.loadAllServices()) {
			listeAPI.put(service.getName(), service);
		}
		
	}
}
