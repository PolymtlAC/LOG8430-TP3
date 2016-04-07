package com.log8430.group9.api;

import java.net.HttpURLConnection;

import com.log8430.group9.models.APIFile;
/**
 * Interface decrivant la structure des API des services de gestion de fichier
 * @author LOG8430 group9
 *
 */
public interface API {
	/**
	 * recuperation du nom du service de gestion de fichier defini par l'API
	 * @return nom du service
	 */
	public String getName();
	/**
	 * determine si le serveur est connécté au service
	 * @return vrai si le serveur est connecté au service
	 */
	public boolean isConnected();
	/**
	 * accesseur en ecriture du token d'authentification au service
	 * @param token d'authentification au service
	 */
	public void setAccessToken(String token);
	/**
	 * accesseur en lecture du token d'authentification au service
	 * @return le token d'authentification
	 */
	public String getAccessToken();
	/**
	 * fonction permettant de demander au service de gestion de fichier distant un token d'authentification
	 * @param code parametre de la requete http de connection au service
	 */
	public void askForToken(String code);
	/**
	 * permet la recuperation de l'arbre de fichier a partir d'un id decrivant le nom du fichier/dossier a recuperer
	 * @param id identificateur du dossier a recuperer dans l'arbre de fichier
	 * @return structure de donnée decrivant un fichier/dossier
	 */
	public APIFile metadata(String id);
}
