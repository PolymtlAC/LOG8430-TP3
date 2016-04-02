package com.log8430.group9.controllers;

import java.io.File;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.log8430.group9.api.API;
import com.log8430.group9.api.APIDropbox;
import com.log8430.group9.api.APIFactory;

@RestController
public class APIController {
	
	/**
	 * Cette url est appelée automatiquement par dropbow une fois que l'utilisateur a autorisé l'application.
	 * Le code fourni permet d'obtenir un token d'accès au compte de l'utilisateur.
	 * Le retour de cette url n'est pas du JSON mais un texte qui est affiché à l'utilisateur dans son navigateur.
	 * 
	 * @param code
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
	
	@RequestMapping("/api/auth")
    public String auth(
    		@RequestParam(value="api") String apiName,
    		@RequestParam(value="code") String code) {
		
		API api = APIFactory.getAPI(apiName);
		api.askForToken(code);
		
    	return "{\"connected\": \""+api.isConnected()+"\", \"code\": \""+api.getAutorisationCode()+"\"}";
    }
	
	@RequestMapping("/api/is_connected")
    public String isConnected(@RequestParam(value="api", defaultValue="server") String apiName) {
		API api = APIFactory.getAPI(apiName);
    	return "{\"connected\": \""+api.isConnected()+"\", \"code\": \""+api.getAutorisationCode()+"\"}";
	}
	
}
