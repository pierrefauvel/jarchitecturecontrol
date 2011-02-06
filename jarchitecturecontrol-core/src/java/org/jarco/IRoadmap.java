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

	// ------------ PRIORITE 

	//TODO V0.1 virer petclinic de subversion
	//TODO V0.1 plan de test 1 : pr�parer des classes fictives pour v�rifier le parsing. Impl�menter l'analyse � la demande. L'�tendre au xml et aux properties
	//TODO V0.1 plan de test 2 : pr�parer une application fictive pour v�rifier l'articulation. Pomper sur bankonet, en plus simple ?  Un cas d'exception par violation possible
	//TODO V0.1 pr�voire une arborescence : data/<project>/{specification,configuration,tagrepository) + possibilit� de cr�er / choisir un projet + import (clonage) d'un des 3 � partir d'un projet existant
	//TODO V0.1 du coup supprimer la notion de s�lection d'une configuration ET de configuration set
	
	// ------------ ENSUITE

	
	//TODO v0.1 mat�rialiser la s�lection
	
	//TODO V0.1 revoir le style de l'�cran principal. Quelque chose de plus graphique, genre "processus" � la ppt
	//TODO V0.1 revoir le style des boutons. Encadrer les icones ?
	//TODO v0.1 ajouter icone pour les rapports et le main panel
	
	//TODO v0.1 m�moriser le projet en cours => le recharger par d�faut (ainsi que les 3 bases)

	//TODO V0.1 dissocier maven ref = la r�f�rence ET maven ref = la r�solution

	//TODO V0.1 am�liorer tous les rapports : possibilit� de filtrer ou de faire des recherche (se baser sur xpath)

	//TODO V0.1 am�liorer dependencies report : les noeuds "dependencies" sont moches. les supprimer ?
	
	//TODO V0.1 javadoc minium en anglais, en profiter pour virer le code mort
	
	//TODO V0.1 tooltips & aide sur tous les noeuds et tous les champs ? doc r�dig�e flottante ?

	//TODO V0.1 prevoir la possibilit� d'activer les rapports compl�mentaires (analysis) ? les ex�cuter � la demande ?
	
	//TODO V0.1 enregistrement auto des modifs (qd on ferme la fenetre, qd on change de noeud ds l'arbre), supprimer le bouton save
	
	//TODO V0.1 export/import partiel : possibilit� d'exporter/importer JUSTE une instance d'entit�

	//TODO V0.1 copies d'�cran + page "screenshots"
	
	//TODO V0.1 doc r�dig�e des tests automatis�s
	
	// TODO v0.1 removeChild A IMPLEMENTER pour TagRepositoryModel

	//------------------------------------------------------------------------------------
	//TODO V1.0 possibilit� de disabler un noeud spec (et ce qui est en dessous) + icone sp�cifique
	
	//TODO V1.0 trouver une ergonomie o� les rapports sont int�gr�s (onglets ou autre) MAIS ouverts � la demande
	
	//TODO V1.0 passer les fichiers xml (configuration, tag repository, specification) � 1 fichier par instance d'entit� et non 1 fichier par classe d'entit�s

	//TODO V1.1 extensibilit� : pouvoir ajouter (par conf) un type de noeud dans les spec. Alimenter la factory, le jtree � partir de cette liste dynamique
	//TODO V1.1 packaging maven pour le build
	//TODO V1.1 version "war" pour l'appli
	//TODO V1.1 fragments (de spec) r�utilisables (notion de "r�f�rence vers un fragment" pour avoir le m�me type de r�gles pour diff�rents patterns)
	
	
	//TODO V1.2 g�n�rer le(s) fichier(s) de conf pour checkstyle, pmd, findbug avec des r�gles diff�renci�es selon le tag 
	
	//TODO V1.0 r�soudre les violations sur le cas d'exemple
	//TODO V0.1 cas d'exemple : Spring Pet Clinic (finir c�t� pr�sentation)
	//TODO V1.1 mettre en place un formatage du code
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
