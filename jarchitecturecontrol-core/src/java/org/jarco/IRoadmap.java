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

	// ------------ PRIORITE 

	//TODO V0.1 virer petclinic de subversion
	//TODO V0.1 plan de test 1 : préparer des classes fictives pour vérifier le parsing. Implémenter l'analyse à la demande. L'étendre au xml et aux properties
	//TODO V0.1 plan de test 2 : préparer une application fictive pour vérifier l'articulation. Pomper sur bankonet, en plus simple ?  Un cas d'exception par violation possible
	//TODO V0.1 prévoire une arborescence : data/<project>/{specification,configuration,tagrepository) + possibilité de créer / choisir un projet + import (clonage) d'un des 3 à partir d'un projet existant
	//TODO V0.1 du coup supprimer la notion de sélection d'une configuration ET de configuration set
	
	// ------------ ENSUITE

	
	//TODO v0.1 matérialiser la sélection
	
	//TODO V0.1 revoir le style de l'écran principal. Quelque chose de plus graphique, genre "processus" à la ppt
	//TODO V0.1 revoir le style des boutons. Encadrer les icones ?
	//TODO v0.1 ajouter icone pour les rapports et le main panel
	
	//TODO v0.1 mémoriser le projet en cours => le recharger par défaut (ainsi que les 3 bases)

	//TODO V0.1 dissocier maven ref = la référence ET maven ref = la résolution

	//TODO V0.1 améliorer tous les rapports : possibilité de filtrer ou de faire des recherche (se baser sur xpath)

	//TODO V0.1 améliorer dependencies report : les noeuds "dependencies" sont moches. les supprimer ?
	
	//TODO V0.1 javadoc minium en anglais, en profiter pour virer le code mort
	
	//TODO V0.1 tooltips & aide sur tous les noeuds et tous les champs ? doc rédigée flottante ?

	//TODO V0.1 prevoir la possibilité d'activer les rapports complémentaires (analysis) ? les exécuter à la demande ?
	
	//TODO V0.1 enregistrement auto des modifs (qd on ferme la fenetre, qd on change de noeud ds l'arbre), supprimer le bouton save
	
	//TODO V0.1 export/import partiel : possibilité d'exporter/importer JUSTE une instance d'entité

	//TODO V0.1 copies d'écran + page "screenshots"
	
	//TODO V0.1 doc rédigée des tests automatisés
	
	// TODO v0.1 removeChild A IMPLEMENTER pour TagRepositoryModel

	//------------------------------------------------------------------------------------
	//TODO V1.0 possibilité de disabler un noeud spec (et ce qui est en dessous) + icone spécifique
	
	//TODO V1.0 trouver une ergonomie où les rapports sont intégrés (onglets ou autre) MAIS ouverts à la demande
	
	//TODO V1.0 passer les fichiers xml (configuration, tag repository, specification) à 1 fichier par instance d'entité et non 1 fichier par classe d'entités

	//TODO V1.1 extensibilité : pouvoir ajouter (par conf) un type de noeud dans les spec. Alimenter la factory, le jtree à partir de cette liste dynamique
	//TODO V1.1 packaging maven pour le build
	//TODO V1.1 version "war" pour l'appli
	//TODO V1.1 fragments (de spec) réutilisables (notion de "référence vers un fragment" pour avoir le même type de règles pour différents patterns)
	
	
	//TODO V1.2 générer le(s) fichier(s) de conf pour checkstyle, pmd, findbug avec des règles différenciées selon le tag 
	
	//TODO V1.0 résoudre les violations sur le cas d'exemple
	//TODO V0.1 cas d'exemple : Spring Pet Clinic (finir côté présentation)
	//TODO V1.1 mettre en place un formatage du code
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
