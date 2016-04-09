package com.log8430.group9.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ServiceLoader extends ClassLoader{
	public ServiceLoader(ClassLoader parent) {
		super(parent);
	}


	/**
	 * Load a class of the commands folder.
	 * 
	 * @return a Class of the commands folder
	 */
	public Class loadClass(String name) throws ClassNotFoundException {
		if(!name.contains("API") || name.contains("com.log8430.group9.models") || name.equals("com.log8430.group9.api.API") || name.equals("com.log8430.group9.api.AbstractAPI") || name.equals("com.log8430.group9.api.APIFactory")) {
			return super.loadClass(name);
		}

		try {
			InputStream input = new FileInputStream("Services/"+name+".class");
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();

			while(data != -1){
				buffer.write(data);
				data = input.read();
			}

			input.close();

			byte[] classData = buffer.toByteArray();

			return this.defineClass("com.log8430.group9.api."+name, classData, 0, classData.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an instance of a API class loaded dynamically.
	 * 
	 * @param name the file name of the service without extension
	 * @return an instance of the service Class
	 */
	public API loadService(String name) {
		try {
			Class serviceClass = this.loadClass(name);
			return (API) serviceClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads all services located in the Services directory.
	 * 
	 * @return a list of services instances
	 */
	public ArrayList<API> loadAllServices() {
		ArrayList<API> services = new ArrayList<>();
		String filePath = System.getProperty("user.dir")+"/Services";
		
		filePath = filePath.replace("\\", "\\\\");
		File commandFolder = new File(filePath);
		if(commandFolder.listFiles() != null){
			System.out.println("nombre de sousfichier : " + commandFolder.listFiles().length);
			for(File commandClassFile : commandFolder.listFiles()) {
				if(commandClassFile.getName().contains("API")) {
					services.add(this.loadService(commandClassFile.getName().replaceFirst("[.][^.]+$", "")));
				}
			}
		}
		return services;
	}

}
