package com.log8430.group9;

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
	@RequestMapping("/dropbox/code")
    public String dropboxCode(@RequestParam(value="code", defaultValue="server") String code) {
		APIDropbox apiDropbox = (APIDropbox) APIFactory.getAPI("dropbox");
		apiDropbox.setToken(null);
		String result = apiDropbox.post("https://api.dropboxapi.com/1/oauth2/token", "grant_type=authorization_code"
				+ "&client_id=0b5l8skd2z5xujs&client_secret=gha8o37bytj0wae"
				+ "&redirect_uri=http://localhost:8080/dropbox/code"
				+ "&code="+code);
		
		JSONObject json = new JSONObject(result);
		apiDropbox.setToken(json.getString("access_token"));
		
    	return "Done. You can now return to the application.";
    }
	
	@RequestMapping("/api/is_connected")
    public String isConnected(@RequestParam(value="api", defaultValue="server") String apiName) {
		API api = APIFactory.getAPI(apiName);
    	return "{\"connected\": \""+api.isConnected()+"\"}";
	}
	
}
