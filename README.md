# LOG8430-TP3 : Mise en Oeuvre d'une Architecture Logicielle - Applications distribuées
## Option 1 : Gestion de fichier
Alexandre Chenieux - Thomas Neyraut - Alexandre Pereira

Ce logiciel permet d'exécuter des commandes sur des dossiers et des fichiers. Une arborescence permet à l'utilisateur de naviguer à travers les dossiers et les fichiers. Pour pouvoir exécuter une commande l'utilisateur doit préalablement sélectionner un dossier ou un fichier. Initialement, la racine sélectionnée par le logiciel et le répertoire "Fichier" du serveur. Enfin via l'interface graphique, l'utilisateur peut choisir une autre racine pour l'arborescence entre le serveur, son compte Dropbox ou son compte Google Drive.

Certaines commandes ne peuvent être exécuter que pour des fichiers ou des dossiers. Dans ce cas, l'interface graphique s'adapte et certaines commandes sont "grisées" et désactivées. Si l'option "AutoRun" est activé, l'exécution des commandes accessibles est automatique dès la sélection d'un dossier ou d'un fichier. Enfin, un bouton clear permet d'effacer les différents résultats des commandes.

Les commandes sont chargés automatiquement au lancement du serveur. L'utilisateur peut utiliser l'interface Command.java afin de concevoir ses propres commandes.

Exemple d'une commande retournant le nom d'un fichier : 

import java.io.File;
public class FileNameCommand implements Command {

// méthode permettant de définir la commande (résultat de la commande)
@Override
public String execute(File file) {
return file.getName();
}

// méthode définissant si la commande est exécutable pour un fichier
@Override
public boolean fileCompatible() {
return true;
}

// méthode définissant si la commande est exécutable pour un dossier
@Override
public boolean folderCompatible() {
return false;
}

// méthode définissant le nom de la commande apparaissant sur l'inteface graphique
@Override
public String getName() {
return "File name";
}

}


### Have Fun !
