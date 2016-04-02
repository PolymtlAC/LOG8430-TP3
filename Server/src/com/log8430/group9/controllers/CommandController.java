package com.log8430.group9.controllers;

import java.io.File;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.log8430.group9.api.API;
import com.log8430.group9.api.APIFactory;
import com.log8430.group9.models.APIFile;

@RestController
public class CommandController {

    @RequestMapping("/command/metadata")
    public APIFile tree(
    		@RequestParam(value="api", defaultValue="server") String apiName,
    		@RequestParam(value="path") String path) {
    	
    	API api = APIFactory.getAPI(apiName);
    	return api.metadata(path);
    }
    
}
