package org.jarco.test;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IProject;
import org.jarco.control.specifications.model.AndPredicate;
import org.jarco.control.specifications.model.AnnotationPredicate;
import org.jarco.control.specifications.model.FollowAssertion;
import org.jarco.control.specifications.model.FollowProductionRule;
import org.jarco.control.specifications.model.ModifierPredicate;
import org.jarco.control.specifications.model.NamePredicate;
import org.jarco.control.specifications.model.NotPredicate;
import org.jarco.control.specifications.model.OrAssertion;
import org.jarco.control.specifications.model.PredicateAssertion;
import org.jarco.control.specifications.model.Specification;
import org.jarco.control.specifications.model.TagAffectation;
import org.jarco.control.specifications.model.FollowAssertion.CardinalityMax;
import org.jarco.control.specifications.model.FollowAssertion.CardinalityMin;

public class SpecficationDeTest extends Specification<ICodeRepository> {

	private TagRepositoryDeTest test;
	
	public SpecficationDeTest(TagRepositoryDeTest test) {
		this.test=test;
		
//		Specification s_projets = new Specification( new FollowProductionRule ("Projects"));
//		addChildSpec(s_projets);
		
		Specification s_projet = new Specification(
				new FollowProductionRule("Projects"),
				new NamePredicate("test-analysis-","version",""));
		addChildSpec(s_projet);
		Specification s_packages = new Specification(
				new FollowProductionRule("Packages"),
				new NamePredicate("org.jarco.test.analysis","domain","")
		);
		s_projet.addChildSpec(s_packages);
		Specification s_p_classes = new Specification(
				new FollowProductionRule("Classes")
		);
		s_packages.addChildSpec(s_p_classes);
		
		Specification<IClass> s_p_applicative_code = new Specification( new NamePredicate("Classe","suffix",""));
		s_p_classes.addChildSpec(s_p_applicative_code);
		s_p_applicative_code.addConsequence(new TagAffectation(test,test.isApplicativeCode));

		Specification<IClass> s_p_applicative_code2 = new Specification( new NamePredicate("Interface","suffix",""));
		s_p_classes.addChildSpec(s_p_applicative_code2);
		s_p_applicative_code2.addConsequence(new TagAffectation(test,test.isApplicativeCode));

		Specification<IField> s_p_ac_f = new Specification<IField>( new FollowProductionRule("DeclaredFields"));
		s_p_applicative_code.addChildSpec(s_p_ac_f);
		
		OrAssertion<IField> oa = new OrAssertion<IField>();
		AndPredicate<IField> ap1 = new AndPredicate<IField>();
		ap1.add(new ModifierPredicate<IField>(EModifier._public));
		ap1.add(new ModifierPredicate<IField>(EModifier._static));
		ap1.add(new ModifierPredicate<IField>(EModifier._final));
		AndPredicate<IField> ap2 = new AndPredicate<IField>();
		ap2.add(new ModifierPredicate<IField>(EModifier._private));
		ap2.add(new NotPredicate<IField>(new ModifierPredicate<IField>(EModifier._static)));
		ap2.add(new AnnotationPredicate<IField>("org.jarco.test.analysis.packageA.Property"));
		oa.addAssertion(new PredicateAssertion("Constante",ap1));
		oa.addAssertion(new PredicateAssertion("Propriete",ap2));
		s_p_ac_f.addAssertion(oa);

		Specification<IClass> s_p_annotations = new Specification( new ModifierPredicate<IClass>(EModifier._annotation));
		s_p_classes.addChildSpec(s_p_annotations);
		s_p_annotations.addConsequence(new TagAffectation(test,test.isAnnotation));
		
		Specification<IMethod> s_p_ac_m = new Specification(new FollowProductionRule("DeclaredMethods"));
		s_p_ac_m.addAssertion(new FollowAssertion("Pas d'utilisation de System.out","ReadsFields",
				new NamePredicate("out","",""),CardinalityMin._0,CardinalityMax._0));
		s_p_applicative_code.addChildSpec(s_p_ac_m);
		
	}

}
