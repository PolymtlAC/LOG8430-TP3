package com.log8430.group9.api;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/**
 * Liste l'ensemble des APIs des services de gestion de fichier gerer par le serveur
 * pattern singleton avec systeme de changement comportemental
 * @author Alexandre
 *
 */
public class APIFactory{
	/**
	 * instance unique de la classe APIFactory
	 */
	protected static APIFactory instance = null;
	/**
	 * liste des APIs accessibles par la systeme
	 */
	protected HashMap<String, API> listeAPI = new HashMap<>();

	
	
	/**
	 * retourne l'instance de l'API relative au service de gestion de fichier demandé
	 * @param apiName : nom du service de fichier demandé
	 * @return l'API du service de gestion de fichier
	 */
	protected APIFactory(){
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
	public static APIFactory getInstance(){
		if(instance == null){
				instance = new APIFactory();
		}
		return instance;
	}
	/**
	 * fonction permettant d'ajouter un service de fichier a la liste disponible
	 * @param api classe implementant l'API du service
	 * @param nomAPI nom du service
	 */
	public void addAPI(API api, String nomAPI){
		listeAPI.put(nomAPI, api);
	}
	/**
	 * permet la recuperation de la liste des noms des services
	 * @return la liste des noms des service de gestion de fichier
	 */
	public Iterator<String> getListeNomsAPI(){
		return listeAPI.keySet().iterator();
	}
	/**
	 * fonction demandant la recuperation de tout les services gestion de fichiers disponible
	 */
	protected void loadServices() {
		listeAPI.clear();
		ServiceLoader serviceLoader = new ServiceLoader(ServiceLoader.class.getClassLoader());
		for(API service :serviceLoader.loadAllServices()) {
			listeAPI.put(service.getName(), service);
		}
		
	}
	
}
