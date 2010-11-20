package org.jarco.xml;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.jarco.control.specifications.model.AnnotationPredicate;
import org.jarco.control.specifications.model.ChainedProductionRule;
import org.jarco.control.specifications.model.ContextAffectationFromProperties;
import org.jarco.control.specifications.model.ContextAffectationFromXpath;
import org.jarco.control.specifications.model.FilterFromXpath;
import org.jarco.control.specifications.model.FollowAssertion;
import org.jarco.control.specifications.model.FollowProductionRule;
import org.jarco.control.specifications.model.ModifierPredicate;
import org.jarco.control.specifications.model.NamePredicate;
import org.jarco.control.specifications.model.OrAssertion;
import org.jarco.control.specifications.model.PredicateAssertion;
import org.jarco.control.specifications.model.Specification;
import org.jarco.control.specifications.model.TagAffectation;
import org.jarco.control.specifications.model.TagPredicate;
import org.jarco.tags.external.ITagRepository;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

//TODO V1.1 FromXmlFactory doit générer le schéma
public class SpecificationFromXmlFactory extends FromXmlFactory {
	
	private ITagRepository tagRepo;
	
	
	
	public SpecificationFromXmlFactory (ITagRepository tagRepo)
	{
		this.tagRepo=tagRepo;
		register(AnnotationPredicate.class);
		register(ChainedProductionRule.class);
		register(ContextAffectationFromProperties.class);
		register(ContextAffectationFromXpath.class);
		register(FilterFromXpath.class);
		register(FollowAssertion.class);
		register(FollowProductionRule.class);
		register(ModifierPredicate.class);
		register(NamePredicate.class);
		register(OrAssertion.class);
		register(PredicateAssertion.class);
		register(Specification.class);
		register(TagAffectation.class);
		register(TagPredicate.class);
	}


	public ITagRepository getTagRepository() {
		return tagRepo;
	}
}
