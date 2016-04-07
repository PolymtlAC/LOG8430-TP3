package com.log8430.group9.api;
/**
 * Liste l'ensemble des APIs des services de gestion de fichier gerer par le serveur
 * @author Alexandre
 *
 */
public class APIFactory {
	/**
	 * API du service de gestion de fichier Dropbox
	 */
	private static APIDropbox apiDropbox = new APIDropbox();
	/**
	 * API du service de gestion de fichier GoogleDrive
	 */
	private static APIGoogleDrive apiGoogleDrive = new APIGoogleDrive();
	/**
	 * API du service de gestion de fichier interne au serveur
	 */
	private static APIServer apiServer = new APIServer();
	/**
	 * retourne l'instance de l'API relative au service de gestion de fichier demandé
	 * @param apiName : nom du service de fichier demandé
	 * @return l'API du service de gestion de fichier
	 */
	public static API getAPI(String apiName) {
		switch(apiName) {
			case "dropbox":
				return apiDropbox;
			case "googledrive":
				return apiGoogleDrive;
			default:
				return apiServer;
		}
	}
}
