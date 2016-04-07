package com.log8430.group9.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.log8430.group9.models.APIFile;
/**
 * classe abstraite definissant le comportement par defaut et commun au API
 * defini les signatures des fonctions que doit implémenter une nouvelle API.
 * @author LOG8430 group9
 *
 */
public abstract class AbstractAPI implements API {
	/**
	 * jeton d'authentification pour les services de gestion de fichier en ligne
	 */
	protected String token = null;
	/**
	 * fonction permettant de recuperer les parametres de la connection au service de gestion de ichier.
	 * recupere les authorisations en utilisant le token recupereé
	 * @param connection objet representant la connection au service de gestion de fichier
	 */
	public abstract void addConnectionProperties(HttpURLConnection connection);
	
	/**
	 * implementation de la methode http get. 
	 * @param targetURL URL du service devant repondre a la requete
	 * @param urlParameters : parametres de la requete
	 * @return la reponse du service pour la requete spécifié
	 */
	public String get(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(targetURL + "?" + urlParameters);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			
			this.addConnectionProperties(connection);
			
			return request(connection);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	/**
	 * implementation de la methode http post. 
	 * @param targetURL URL du service devant repondre a la requete
	 * @param urlParameters : parametres de la requete
	 * @return la reponse du service pour la requete spécifié
	 */
	public String post(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;  
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			this.addConnectionProperties(connection);
			connection.setDoOutput(true);
			
			//Send request
			OutputStream wr = connection.getOutputStream();
			wr.write(urlParameters.getBytes("UTF-8"));
			wr.close();
			
			return request(connection);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	/**
	 * fonction emettant la requete au service spécifié via la connection
	 * recupere la reponse du service sous forme de chaine de caractere
	 * @param connection requete au service de gestion de fichier spécifié
	 * @return String representant la reponse du serveur a la requete
	 */
	public String request(HttpURLConnection connection) {
		try {
			InputStream is;
			if (connection.getResponseCode() >= 400) {
			    is = connection.getErrorStream();
			} else {
			    is = connection.getInputStream();
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
		    }
		    rd.close();
		    
		    return response.toString();
		    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
