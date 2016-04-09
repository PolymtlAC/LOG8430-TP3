package com.log8430.group9.tests;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

import com.log8430.group9.Application;
import com.log8430.group9.api.API;
import com.log8430.group9.api.APIDropbox;
import com.log8430.group9.api.APIFactory;
/**
 * create a running instance of the server with mocking instance of the external file services
 * @author TP8430 group9
 *
 */
import com.log8430.group9.api.APIGoogleDrive;
import com.log8430.group9.api.AbstractAPI;
public class ServeurTests{
	
	@Before
	protected void setUp() throws Exception {
		EasyMock.createMock(AbstractAPI.class);
		EasyMock.createMock(AbstractAPI.class);
		EasyMock.createMock(AbstractAPI.class);
		//APIFactory apiFact = new APIFactoryMocked();
		
	}
	
	 public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	}

}
