package com.log8430.group9;

import java.io.File;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController {

    @RequestMapping("/command/tree")
    public APIFile tree(@RequestParam(value="api", defaultValue="server") String api) {
    	File file = new File(System.getProperty("user.dir")+"/root");
    	APIFile apiFile = new APIFile(file);
    	apiFile.setChildren(file);
    	return apiFile;
    }
    
}
