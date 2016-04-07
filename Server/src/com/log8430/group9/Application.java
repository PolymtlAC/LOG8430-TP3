package com.log8430.group9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * classe principale, contient la fonction main qui lance l'application serveur
 * @author LOG8430 group9
 *
 */
@SpringBootApplication
public class Application {
/**
 * main : demande le lancement de l'application serveur
 * @param args     
 */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}