package com.log8430.group9.main;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.log8430.group9.views.MainWindow;
/**
 * classe principale de l'application
 * @author LOG8430 group9
 *
 */
public class Main {
/**
 * methode main pour lancer l'application
 * @param args
 */
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setVisible(true);
	}

}
