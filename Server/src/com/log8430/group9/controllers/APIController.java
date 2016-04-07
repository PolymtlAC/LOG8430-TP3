package com.log8430.group9.controllers;

import java.io.File;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.log8430.group9.api.API;
import com.log8430.group9.api.APIDropbox;
import com.log8430.group9.api.APIFactory;
/**
 * controller pour les requetes avec les services de gestion de fichier
 * @author LOG8430 group9
 *
 */
@RestController
public class APIController {
	
	/**
	 * Cette url est appelée automatiquement par dropbow une fois que l'utilisateur a autorisé l'application.
	 * Le code fourni permet d'obtenir un token d'accès au compte de l'utilisateur.
	 * Le retour de cette url n'est pas du JSON mais un texte qui est affiché à l'utilisateur dans son navigateur.
	 * 
	 * @param code parametre de la requete
	 * @return Un message affiché dans le navigateur de l'utilisateur
	 */
	@RequestMapping("/api/code")
    public String code(
    		@RequestParam(value="api") String apiName,
    		@RequestParam(value="code") String code) {
		
		API api = APIFactory.getAPI(apiName);
		api.askForToken(code);
		
    	return "Done. You can now return to the application.";
    }
	/**
	 * URL pour l'authentification au service de gestion de fichier
	 * @param apiName nom du service
	 * @param token jeton d'authentification
	 * @return Structure contenant l'etat de la connexion et le jeton d'authentification au service
	 */
	@RequestMapping("/api/auth")
    public String auth(
    		@RequestParam(value="api") String apiName,
    		@RequestParam(value="token") String token) {
		
		API api = APIFactory.getAPI(apiName);
		api.setAccessToken(token);
		
    	return "{\"connected\": \""+api.isConnected()+"\", \"token\": \""+api.getAccessToken()+"\"}";
    }
	/**
	 * recupere l'etat de la connexion au service
	 * @param apiName nom du service
	 * @return l'etat de la connexion ainsi que le jeton d'authentification
	 */
	@RequestMapping("/api/is_connected")
    public String isConnected(@RequestParam(value="api", defaultValue="server") String apiName) {
		API api = APIFactory.getAPI(apiName);
    	return "{\"connected\": \""+api.isConnected()+"\", \"token\": \""+api.getAccessToken()+"\"}";
	}
	
}
