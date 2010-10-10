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

	//TODO V1 résoudre les violations sur le cas d'exemple
	//TODO V1 cas d'exemple : Spring Pet Clinic

	//TODO V1.1 livrer cas de tests spécifiques pour chaque méthode de chaque classe (= cas d'exemple)
	//TODO V1.1 livrer : sources (commentées)
	//TODO V1.1 Reporting en texte riche. API hiérarchique + style. Implémentation de base en HTML
	//TODO V1.1 Dissocier plus clairement code : internal vs reflect vs bcel vs maven
	//TODO V1.1 Gestion des classes englobées dans une méthode
	
	//TODO V1.2 Améliorer la définition des specs (dsl ? xml ? )
	//TODO V1.2 package pour utilisation dans maven & sonar
	//TODO V1.2 systeme de cache + utilisation e reference pour la grappe read-only (codemodel.*)
	//TODO V1.2 faire une implémentation baséee sur un projet eclipse
	
	//TODO V2 système de filtre a piori (pour les requetes)
	//TODO V2 remplacer bcel par javassist ou objectweb asm
	//TODO V2 intégrer un moteur de règles
	//TODO V2 voir le produit de contrôle statique (ETPT Eclipse)
	//TODO V2 regarder le format qdoc (nom à corriger)
	//TODO V2 traiter aussi les fichiers  .jsp (parsing approfondi), ...
	//TODO V2 Envisager velocity pour les expressions ou un autre langage de script (bean shell ? ognl ?)
	//TODO V2 Version compatible OSGI
	//TODO V2 Version compatible IVY
	
	//TODO V3 exploiter l'api d'analyse syntaxique de java d'eclipse
	//TODO V3 implémenter un framework de parsing dynamique (cf aikido) + grammaire java => règles portant sur le code source
	//TODO V3 créer les alertes dans Eclipse
	//TODO V3 Implémenter une inversion (par qui est appelée une méthode), en définissant un espace de recherche
	

	
}
