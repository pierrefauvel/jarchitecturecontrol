package org.jarco;

public interface IRoadmap {

	/**
	 * > But : ce que l'on veut contr�ler
	 * > Cercle vertueux : 
	 * 		r�gle, 
	 * 		v�rification,
	 * 		violation,
	 * 		correction ou n�gociation transparente, 
	 * 		mise � jour des r�gles
	 * > Moteur :
	 * 		d�finition des specs
	 * 		chargement des projets
	 * 		execution des specs (avec analyses reflection ou bcel � la vol�e)
	 * 		rapports
	 * > Illustration
	 * 		Cas d'exemple
	 * 		Sp�cifications (rapports color�)
	 * 		R�sultats (rapports color�s)
	 */

	//TODO V0.1 reprendre TOUS les rapports historiques. Soit int�gr� � Swing Soit fichier que l'on peut ouvrir depuis Swing (ou naviguer dans, car c'est du html)
	//TODO V0.1 rapport de contr�le : NE PAS TOUT CHARGER EN MEMOIRE. il faudrait un fichier + une vue "arbre" qui permette de ne charger que ce dont on a besoin (deferred loading)
	//TODO V0.1 rapport de d�pendencies : rapport HTML ? => fichier + visu simple en html (xslt)
	//TODO V0.1 specification : rapport HTML => fichier + visu simple en html (xslt)
	//TODO V0.1 tags : vision structur�e swing (arbre + d�tail avec les attributs)
	//TODO V0.1 violations : export xml (en utilisant la plomberie et en la g�n�ralisant) PUIS restauration par la GUI
	//TODO V0.1 pr�voir un export du r�sultat d'ex�cution dans un fichier zip (tous les rapports) + la possibilit� de charger de rapport et de le consulter avec le client graphique => ajouter une �tape : control => tout s'ecrit sur le systeme de fichier => je lance la visualisation graphique
	
    //TODO V0.1 passer de 1 tag repository � N tag repository
    //TODO V0.1 passer de 1 specification � N specifications
    //TODO V0.1 trouver un moyen de nommer les diff�rentes entit�s principales (cfg,tr,spec) => faire appara�tre le nom de l'entit� s�lectionn�e
    //TODO V1.0 passer les fichiers xml (configuration, tag repository, specification) � 1 fichier par instance d'entit� et non 1 fichier par classe d'entit�s

	//TODO V1.1 extensibilit� : pouvoir ajouter (par conf) un type de noeud dans les spec. Alimenter la factory, le jtree � partir de cette liste dynamique
	//TODO V1.1 packaging maven pour le build
	//TODO V1.1 version "war" pour l'appli
	//TODO V1.1 fragments (de spec) r�utilisables (notion de "r�f�rence vers un fragment" pour avoir le m�me type de r�gles pour diff�rents patterns)
	
	
	//TODO V1.2 g�n�rer le(s) fichier(s) de conf pour checkstyle, pmd, findbug avec des r�gles diff�renci�es selon le tag 
	
	//TODO V1.0 r�soudre les violations sur le cas d'exemple
	//TODO V0.1 cas d'exemple : Spring Pet Clinic (finir c�t� pr�sentation)
	//TODO V1.1 mettre en place un formatage du code
	//TODO V0.1 compl�ter les icones => configuration, tag editor
	//TODO V1.1 implementer un clone "deep" en utilisant la serialization xml

	//TODO V1.1 dissocier swing de la logique (en vue du pilotage via maven)
	//TODO V1.1 passer un minimum Sonar sur le code
	//TODO V1.1 livrer cas de tests sp�cifiques pour chaque m�thode de chaque classe (= cas d'exemple)
	//TODO V1.1 G�n�raliser les rapports (pour l'instant l'IHM montre seulement les violations)
	//TODO V1.1 Reporting en texte riche. API hi�rarchique + style. Impl�mentation de base en HTML

	//TODO V1.2 Gestion des classes englob�es dans une m�thode
	//TODO V1.2 systeme de cache + utilisation e reference pour la grappe read-only (codemodel.*)
	//TODO V1.2 package pour utilisation dans maven & sonar

	//TODO V2 faire une impl�mentation bas�ee sur un projet eclipse
	//TODO V2 syst�me de filtre a piori (pour les requetes)
	//TODO V2 remplacer bcel par javassist ou objectweb asm
	//TODO V2 traiter aussi les fichiers  .jsp (parsing approfondi), ...
	//TODO V2 Envisager velocity pour les expressions ou un autre langage de script (bean shell ? ognl ?)
	//TODO V2 Impl�menter une inversion (par qui est appel�e une m�thode), en d�finissant un espace de recherche
	
	//TODO V2 int�grer un moteur de r�gles
	//TODO V2 voir le produit de contr�le statique (ETPT Eclipse)
	//TODO V3 Version compatible OSGI
	//TODO V3 Version compatible IVY
	//TODO V3 exploiter l'api d'analyse syntaxique de java d'eclipse
	//TODO V3 impl�menter un framework de parsing dynamique (cf aikido) + grammaire java => r�gles portant sur le code source
	//TODO V3 cr�er les alertes dans Eclipse
	

	
}
