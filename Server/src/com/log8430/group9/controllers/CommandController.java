package com.log8430.group9.controllers;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.log8430.group9.api.API;
import com.log8430.group9.api.APIFactory;
import com.log8430.group9.models.APIFile;
/**
 * Controller pour les commandes utilisateur
 * @author LOG8430 group9 
 */
@RestController
public class CommandController {
	/**
	 * recuperation du fichier selectionné par l'utilisateur pour l'execution de la commande
	 * @param apiName nom du service de gestion de fichier
	 * @param id identificateur du fichier a charger
	 * @return strucuture de donnée decrivant le fichier
	 */
    @RequestMapping("/command/metadata")
    public APIFile tree(
    		@RequestParam(value="api", defaultValue="server") String apiName,
    		@RequestParam(value="id") String id) {
    	
    	API api = APIFactory.getInstance().getAPI(apiName);
    	return api.metadata(id);
    }
    @RequestMapping("/command/listeService")
    public Iterator<String> liste(){
    	return APIFactory.getInstance().getListeNomsAPI();
    }
    
}
