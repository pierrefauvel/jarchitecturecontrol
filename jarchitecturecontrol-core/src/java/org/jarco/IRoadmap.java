package org.jarco;

public interface IRoadmap {

	/**
	 * > But : ce que l'on veut contrôler
	 * > Cercle vertueux : 
	 * 		règle, 
	 * 		vérification,
	 * 		violation,
	 * 		correction ou négociation transparente, 
	 * 		mise à jour des règles
	 * > Moteur :
	 * 		définition des specs
	 * 		chargement des projets
	 * 		execution des specs (avec analyses reflection ou bcel à la volée)
	 * 		rapports
	 * > Illustration
	 * 		Cas d'exemple
	 * 		Spécifications (rapports coloré)
	 * 		Résultats (rapports colorés)
	 */

	//TODO V0.1 reprendre TOUS les rapports historiques. Soit intégré à Swing Soit fichier que l'on peut ouvrir depuis Swing (ou naviguer dans, car c'est du html)
	//TODO V0.1 rapport de contrôle : NE PAS TOUT CHARGER EN MEMOIRE. il faudrait un fichier + une vue "arbre" qui permette de ne charger que ce dont on a besoin (deferred loading)
	//TODO V0.1 rapport de dépendencies : rapport HTML ? => fichier + visu simple en html (xslt)
	//TODO V0.1 specification : rapport HTML => fichier + visu simple en html (xslt)
	//TODO V0.1 tags : vision structurée swing (arbre + détail avec les attributs)
	//TODO V0.1 violations : export xml (en utilisant la plomberie et en la généralisant) PUIS restauration par la GUI
	//TODO V0.1 prévoir un export du résultat d'exécution dans un fichier zip (tous les rapports) + la possibilité de charger de rapport et de le consulter avec le client graphique => ajouter une étape : control => tout s'ecrit sur le systeme de fichier => je lance la visualisation graphique
	
    //TODO V0.1 passer de 1 tag repository à N tag repository
    //TODO V0.1 passer de 1 specification à N specifications
    //TODO V0.1 trouver un moyen de nommer les différentes entités principales (cfg,tr,spec) => faire apparaître le nom de l'entité sélectionnée
    //TODO V1.0 passer les fichiers xml (configuration, tag repository, specification) à 1 fichier par instance d'entité et non 1 fichier par classe d'entités

	//TODO V1.1 extensibilité : pouvoir ajouter (par conf) un type de noeud dans les spec. Alimenter la factory, le jtree à partir de cette liste dynamique
	//TODO V1.1 packaging maven pour le build
	//TODO V1.1 version "war" pour l'appli
	//TODO V1.1 fragments (de spec) réutilisables (notion de "référence vers un fragment" pour avoir le même type de règles pour différents patterns)
	
	
	//TODO V1.2 générer le(s) fichier(s) de conf pour checkstyle, pmd, findbug avec des règles différenciées selon le tag 
	
	//TODO V1.0 résoudre les violations sur le cas d'exemple
	//TODO V0.1 cas d'exemple : Spring Pet Clinic (finir côté présentation)
	//TODO V1.1 mettre en place un formatage du code
	//TODO V0.1 complêter les icones => configuration, tag editor
	//TODO V1.1 implementer un clone "deep" en utilisant la serialization xml

	//TODO V1.1 dissocier swing de la logique (en vue du pilotage via maven)
	//TODO V1.1 passer un minimum Sonar sur le code
	//TODO V1.1 livrer cas de tests spécifiques pour chaque méthode de chaque classe (= cas d'exemple)
	//TODO V1.1 Généraliser les rapports (pour l'instant l'IHM montre seulement les violations)
	//TODO V1.1 Reporting en texte riche. API hiérarchique + style. Implémentation de base en HTML

	//TODO V1.2 Gestion des classes englobées dans une méthode
	//TODO V1.2 systeme de cache + utilisation e reference pour la grappe read-only (codemodel.*)
	//TODO V1.2 package pour utilisation dans maven & sonar

	//TODO V2 faire une implémentation baséee sur un projet eclipse
	//TODO V2 système de filtre a piori (pour les requetes)
	//TODO V2 remplacer bcel par javassist ou objectweb asm
	//TODO V2 traiter aussi les fichiers  .jsp (parsing approfondi), ...
	//TODO V2 Envisager velocity pour les expressions ou un autre langage de script (bean shell ? ognl ?)
	//TODO V2 Implémenter une inversion (par qui est appelée une méthode), en définissant un espace de recherche
	
	//TODO V2 intégrer un moteur de règles
	//TODO V2 voir le produit de contrôle statique (ETPT Eclipse)
	//TODO V3 Version compatible OSGI
	//TODO V3 Version compatible IVY
	//TODO V3 exploiter l'api d'analyse syntaxique de java d'eclipse
	//TODO V3 implémenter un framework de parsing dynamique (cf aikido) + grammaire java => règles portant sur le code source
	//TODO V3 créer les alertes dans Eclipse
	

	
}
