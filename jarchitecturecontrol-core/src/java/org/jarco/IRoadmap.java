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
	//TODO V1 cas d'exemple : Spring Pet Clinic (finir côté présentation)
	//TODO V1 setup éditeur de tag repository
	//TODO V1 intégrer les 3 éditeurs
	//TODO V1 organiser les projets
	//TODO V1 mettre en place un formatage
	//TODO V1 complêter les icones => configuration, tag editor
	//TODO V1 implementer un clone "deep" en utilisant la serialization xml

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
