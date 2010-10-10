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

	//TODO V1 r�soudre les violations sur le cas d'exemple
	//TODO V1 cas d'exemple : Spring Pet Clinic

	//TODO V1.1 livrer cas de tests sp�cifiques pour chaque m�thode de chaque classe (= cas d'exemple)
	//TODO V1.1 livrer : sources (comment�es)
	//TODO V1.1 Reporting en texte riche. API hi�rarchique + style. Impl�mentation de base en HTML
	//TODO V1.1 Dissocier plus clairement code : internal vs reflect vs bcel vs maven
	//TODO V1.1 Gestion des classes englob�es dans une m�thode
	
	//TODO V1.2 Am�liorer la d�finition des specs (dsl ? xml ? )
	//TODO V1.2 package pour utilisation dans maven & sonar
	//TODO V1.2 systeme de cache + utilisation e reference pour la grappe read-only (codemodel.*)
	//TODO V1.2 faire une impl�mentation bas�ee sur un projet eclipse
	
	//TODO V2 syst�me de filtre a piori (pour les requetes)
	//TODO V2 remplacer bcel par javassist ou objectweb asm
	//TODO V2 int�grer un moteur de r�gles
	//TODO V2 voir le produit de contr�le statique (ETPT Eclipse)
	//TODO V2 regarder le format qdoc (nom � corriger)
	//TODO V2 traiter aussi les fichiers  .jsp (parsing approfondi), ...
	//TODO V2 Envisager velocity pour les expressions ou un autre langage de script (bean shell ? ognl ?)
	//TODO V2 Version compatible OSGI
	//TODO V2 Version compatible IVY
	
	//TODO V3 exploiter l'api d'analyse syntaxique de java d'eclipse
	//TODO V3 impl�menter un framework de parsing dynamique (cf aikido) + grammaire java => r�gles portant sur le code source
	//TODO V3 cr�er les alertes dans Eclipse
	//TODO V3 Impl�menter une inversion (par qui est appel�e une m�thode), en d�finissant un espace de recherche
	

	
}
