package org.jarco.test.petclinic;

import java.io.PrintStream;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IXmlDocument;
import org.jarco.code.external.IXmlElement;
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
import org.jarco.control.specifications.model.FollowAssertion.CardinalityMax;
import org.jarco.control.specifications.model.FollowAssertion.CardinalityMin;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagType;
import org.jarco.tags.internal.TagInternal;
import org.jarco.tags.internal.TagRepositoryInternal;

public class PetClinicSpecification extends Specification implements IPetClinicTagConstants{
		
	
	//TODO Speech Rajouter "consistency check"
	//TODO Speech Rajouter "référence depuis les fichiers de conf"
	//TODO Rajouter les contrôles d'appel couche presentation
	public PetClinicSpecification()
	{
		super();

		// petclinic projects
		addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("petclinic-","tier",""))
			{{
				// compute tier
				addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
						{{
							addConsequence(new TagAffectation<IClass>(tr,tg_tier,new String[]{"name","${tier}"}));
						}});
				
				addChildSpec(new Specification<IPackage>(new FollowProductionRule<IPackage>("Packages"))
					{{
					// exception package
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.domain.exception"))
						{{
							addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
							{{
								addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","transversal"}));
								addAssertion(new FollowAssertion<IClass>("ALL CLASSES IN EXCEPTION PACKAGE SHOULD BE EXCEPTIONS","SuperClass",new NamePredicate<IClass>("java.lang.Exception","",""),CardinalityMin._1, CardinalityMax._1));
								addAssertion(new PredicateAssertion<IClass>("ALL CLASSES IN EXCEPTION PACKAGE SHOULD BE NAMED ...EXCEPTION",new NamePredicate<IClass>("","exceptionName","Exception")));
							}});
						}});
					// model package
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.domain.model"))
						{{	
							addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
							{{
								addChildSpec(new Specification<IClass>(new AnnotationPredicate<IClass>("javax.persistence.Entity"))
								{{
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","business-object"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","domain"}));
								}});
								addChildSpec(new Specification<IClass>(new ModifierPredicate<IClass>(EModifier._enum))
								{{
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","enumeration"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","transversal"}));
									addAssertion(new PredicateAssertion<IClass>("ALL ENUMERATIONS SHOULD BE NAMES ...ENUM",new NamePredicate<IClass>("","enumerationName","Enum")));
								}});
							}});
						}});
					// dao itf
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.integration.dao"))
							{{	
								addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"),new NamePredicate<IClass>("","entity","Dao"))
								{{
									addAssertion(new PredicateAssertion<IClass>("ALL DAO SHOULD BE INTERACES",new ModifierPredicate<IClass>(EModifier._interface)));
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","persistence"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","dao"},new String[]{ITagType.ASSOCIATION_TYPE_NAME,"dao"}, new String[]{ITagType.ROLE_TYPE_NAME,"interface"},new String[]{ITagType.ASSOCIATION_NAME,"${entity}"}));
								}});
							}});
					// dao impl
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.integration.dao.impl"))
							{{	
								addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"),new NamePredicate<IClass>("","entity","DaoHibernate"))
								{{
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","persistence"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","dao"},new String[]{ITagType.ASSOCIATION_TYPE_NAME,"dao"}, new String[]{ITagType.ROLE_TYPE_NAME,"implementation"},new String[]{ITagType.ASSOCIATION_NAME,"${entity}"}));

									addChildSpec(new Specification<IField>(new FollowProductionRule<IField>("DeclaredFields"))
											{{
												addAssertion(new PredicateAssertion<IField>("ALL FIELDS IN DAO SHOULD BE STATIC AND FINAL", new ModifierPredicate<IField>(EModifier._static,EModifier._final)));
											}});
								}});
							}});
					// service itf
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.service.business"))
							{{	
								addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"),new NamePredicate<IClass>("","service",""))
								{{
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES IN SERVICE PACKAGE SHOULD BE INTERFACES",new ModifierPredicate<IClass>(EModifier._interface)));
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","service"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","service"},new String[]{ITagType.ASSOCIATION_TYPE_NAME,"service"}, new String[]{ITagType.ROLE_TYPE_NAME,"interface"},new String[]{ITagType.ASSOCIATION_NAME,"${service}"}));
								}});
							}});
					// service impl
					addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.service.business.impl"))
							{{	
								addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"),new NamePredicate<IClass>("","service","Impl"))
								{{
									addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","service"}));
									addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","service"},new String[]{ITagType.ASSOCIATION_TYPE_NAME,"service"}, new String[]{ITagType.ROLE_TYPE_NAME,"implementation"},new String[]{ITagType.ASSOCIATION_NAME,"${service}"}));
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES NAMED ...Impl SHOULD BE ANNOTATED AS SERVICES",new AnnotationPredicate<IClass>("org.springframework.stereotype.Service")));
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES NAMED ...Impl SHOULD BE ANNOTATED AS TRANSACTIONAL",new AnnotationPredicate<IClass>("org.springframework.transaction.annotation.Transactional")));
									addChildSpec(new Specification<IField>(new FollowProductionRule<IField>("DeclaredFields"))
											{{
												addAssertion(new PredicateAssertion<IField>("ALL FIELD IN CLASSES NAMED ...Impl SHOULD BE STATIC FINAL",new ModifierPredicate<IField>(EModifier._static,EModifier._final)));
											}});
								}});
							}});
				// actions
				addChildSpec(new Specification<IPackage>(new NamePredicate<IPackage>("org.springframework.samples.petclinic.web.action","controller",""))
						{{	
							addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"),new NamePredicate<IClass>("","action","Action"))
							{{
								addConsequence(new TagAffectation<IClass>(tr,tg_layer, new String[]{"name","presentation"}));
								addConsequence(new TagAffectation<IClass>(tr,tg_pattern, new String[]{"name","action"}));
								addChildSpec(new Specification<IField>(new FollowProductionRule<IField>("DeclaredFields"))
										{{
											OrAssertion<IField> a = new OrAssertion<IField>();
											a.addAssertion(new PredicateAssertion<IField>("ALL FIELDS IN ACTION SHOULD BE (1) STATIC FINAL",new ModifierPredicate<IField>(EModifier._static,EModifier._final)));
											a.addAssertion(new PredicateAssertion<IField>("ALL FIELDS IN ACTION SHOULD BE (2) AN INJECTED SERVICE",new TagPredicate<IField>(tg_pattern, new String[]{"name","service"})));
											a.addAssertion(new PredicateAssertion<IField>("ALL FIELDS IN ACTION SHOULD BE A MANAGER",new TagPredicate<IField>(tg_pattern, new String[]{"name","manager"})));
											addAssertion(a);
										}});
							}});
						}});
					}});
				addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
						{{
							addAssertion(new PredicateAssertion<IClass>("ALL CLASSES SHOULD BE ASSOCIATED TO A PATTERN", new TagPredicate<IClass>(tg_pattern,new String[][]{}))); 
						}});
				}});

		// references xml & properties
		addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("petclinic-business","version",""))
				{{
					addChildSpec(new Specification<IXmlDocument>(new FollowProductionRule<IXmlDocument>("XmlDocuments"),new NamePredicate<IXmlDocument>("conf-business/dao-context.xml"))
					{{
						addChildSpec(new Specification<IXmlElement>(new FollowProductionRule<IXmlElement>("ChildNodesForXPath", "../beans/bean" ),
									new FilterFromXpath<IXmlElement>("./@id","","dao","Dao"))
								{{
									addConsequence(new ContextAffectationFromXpath<IXmlElement>("reference2classname","./@class"));
									addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().
										chain(new FollowProductionRule<ICodeRepository>("Repository")).
										chain(new FollowProductionRule<IClass>("ClassByName","${reference2classname}")))
										{{
											addConsequence(new TagAffectation<IClass>(tr,tg_referenced_by_spring_as_dao));
									}});
								}});
					}});

					addChildSpec(new Specification<IXmlDocument>(new FollowProductionRule<IXmlDocument>("XmlDocuments"),new NamePredicate<IXmlDocument>("conf-business/hibernate-context.xml"))
							{{
								addChildSpec(new Specification<IXmlElement>(new FollowProductionRule<IXmlElement>("ChildNodesForXPath", "../beans/bean/property[@name=\"annotatedClasses\"]/list/value" ))
										{{
											addConsequence(new ContextAffectationFromXpath<IXmlElement>("reference2classname","./text()"));
											addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().
												chain(new FollowProductionRule<ICodeRepository>("Repository")).
												chain(new FollowProductionRule<IClass>("ClassByName","${reference2classname}")))
												{{
													addConsequence(new TagAffectation<IClass>(tr,tg_referenced_by_spring_as_entity));
											}});
										}});
							}});

					addChildSpec(new Specification<IXmlDocument>(new FollowProductionRule<IXmlDocument>("XmlDocuments"),new NamePredicate<IXmlDocument>("conf-business/service-business-context.xml"))
							{{
								addChildSpec(new Specification<IXmlElement>(new FollowProductionRule<IXmlElement>("ChildNodesForXPath", "../beans/bean"))
										{{
											addConsequence(new ContextAffectationFromXpath<IXmlElement>("reference2classname","./@class"));
											addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().
												chain(new FollowProductionRule<ICodeRepository>("Repository")).
												chain(new FollowProductionRule<IClass>("ClassByName","${reference2classname}")))
												{{
													addConsequence(new TagAffectation<IClass>(tr,tg_referenced_by_spring_as_service));
											}});
										}});
							}});
									
					addChildSpec(new Specification<IPropertiesDocument>(new FollowProductionRule<IPropertiesDocument>("PropertiesDocuments"),new NamePredicate<IPropertiesDocument>("spring-hibernate.properties"))
							{{
									addConsequence(new ContextAffectationFromProperties<IPropertiesDocument>("reference2classname","hibernate.dialect"));
									addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().
										chain(new FollowProductionRule<ICodeRepository>("Repository")).
										chain(new FollowProductionRule<IClass>("ClassByName","${reference2classname}")))
										{{
											addConsequence(new TagAffectation<IClass>(tr,tg_referenced_by_hibernate_as_dialect));
									}});
						}});
				}});

		addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("petclinic-web","version",""))
				{{
					addChildSpec(new Specification<IXmlDocument>(new FollowProductionRule<IXmlDocument>("XmlDocuments"),new NamePredicate<IXmlDocument>("WEB-INF/classes/conf-web/struts-context.xml"))
							{{
								addChildSpec(new Specification<IXmlElement>(new FollowProductionRule<IXmlElement>("ChildNodesForXPath", "../beans/bean" ),
										new FilterFromXpath<IXmlElement>("./@id","","action","Action"))
										{{
											addConsequence(new ContextAffectationFromXpath<IXmlElement>("reference2classname","./@class"));
											addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().
												chain(new FollowProductionRule<ICodeRepository>("Repository")).
												chain(new FollowProductionRule<IClass>("ClassByName","${reference2classname}")))
												{{
													addConsequence(new TagAffectation<IClass>(tr,tg_referenced_by_spring_as_action));
											}});
										}});
							}});
				}});

		// system classes
		addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("System project","",""))
				{{
					addChildSpec(new Specification<IPackage>(new FollowProductionRule<IPackage>("Packages"),new NamePredicate<IPackage>("java.lang"))
					{{
						addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
						{{
							addConsequence(new TagAffectation<IClass>(tr,tg_basicJdk));
						}});
					}});
					addChildSpec(new Specification<IPackage>(new FollowProductionRule<IPackage>("Packages"),new NamePredicate<IPackage>("java.util"))
					{{
						addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
						{{
							addConsequence(new TagAffectation<IClass>(tr,tg_basicJdk));
						}});
					}});
				}});
		addChildSpec(new Specification<IPackage>(new FollowProductionRule<IPackage>("Packages"),new NamePredicate<IPackage>("org.apache.commons.lang.builder","",""))
		{{
			addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
					{{
						addConsequence(new TagAffectation<IClass>(tr,tg_basicApache));
					}});
		}});
		// dependencies check
		addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("petclinic-","tier",""))
				{{
					addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
							{{
								addChildSpec(new Specification<IClass>(new TagPredicate<IClass>(tg_layer,new String[]{"name","domain"}))
								{{
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED DOMAIN AS A LAYER SHOULD BE TAGGED BUSINESS AS A TIER", new TagPredicate<IClass>(tg_tier,new String[]{"name","business"}))); 
									addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().chain(new FollowProductionRule<IMethod>("DeclaredMethods")).chain(new FollowProductionRule<IMethod>("InvokesMethods")).chain(new FollowProductionRule<IClass>("ParentClass")))
									{{
										addAssertion(new OrAssertion<IClass>()
										{{
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED DOMAIN SHOULD INVOKE METHODS ON (1) TRANVERSAL CLASSES", new TagPredicate<IClass>(tg_layer,new String[]{"name","transversal"})));
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED DOMAIN SHOULD INVOKE METHODS ON (2) BASIC JDK CLASSES",new TagPredicate<IClass>(tg_basicJdk)));
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED DOMAIN SHOULD INVOKE METHODS ON (3) BASIC APACHE CLASSES",new TagPredicate<IClass>(tg_basicApache)));
										}});
									}});
								}});
								addChildSpec(new Specification<IClass>(new TagPredicate<IClass>(tg_layer,new String[]{"name","persistence"}))
								{{
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED PERSISTENCE SHOULD BE TAGGED BUSINESS AS A TIER",new TagPredicate<IClass>(tg_tier,new String[]{"name","business"}))); 
									addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().chain(new FollowProductionRule<IMethod>("DeclaredMethods")).chain(new FollowProductionRule<IMethod>("InvokesMethods")).chain(new FollowProductionRule<IClass>("ParentClass")))
									{{
										addAssertion(new OrAssertion<IClass>()
										{{
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED PERSISTENCE SHOULD INVOKE METHODS ON (1) CLASSES TAGGED PERSISTENCE",new TagPredicate<IClass>(tg_layer,new String[]{"name","persistence"})));
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED PERSISTENCE SHOULD INVOKE METHODS ON (2) CLASSES TAGGED TRANSVERSAL",new TagPredicate<IClass>(tg_layer,new String[]{"name","transversal"})));
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED PERSISTENCE SHOULD INVOKE METHODS ON (3) BASIC JDK CLASSES",new TagPredicate<IClass>(tg_basicJdk)));
											addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED PERSISTENCE SHOULD INVOKE METHODS ON (4) BASIC APACHE CLASSES",new TagPredicate<IClass>(tg_basicApache)));
										}});
									}});
								}});
								addChildSpec(new Specification<IClass>(new TagPredicate<IClass>(tg_layer,new String[]{"name","service"}))
								{{
									addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD BE TAGGED BUSINESS AS A TIER",new TagPredicate<IClass>(tg_tier,new String[]{"name","business"}))); 
									addChildSpec(new Specification<IClass>(new ChainedProductionRule<IClass>().chain(new FollowProductionRule<IMethod>("DeclaredMethods")).chain(new FollowProductionRule<IMethod>("InvokesMethods")).chain(new FollowProductionRule<IClass>("ParentClass")))
									{{
										addAssertion(new OrAssertion<IClass>()
												{{
													addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD INVOKE METHODS ON (1) CLASSES TAGGED PERSISTENCE", new TagPredicate<IClass>(tg_layer,new String[]{"name","persistence"})));
													addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD INVOKE METHODS ON (2) CLASSES TAGGED DOMAIN",new TagPredicate<IClass>(tg_layer,new String[]{"name","domain"})));
													addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD INVOKE METHODS ON (3) CLASSES TAGGED TRANSVERSAL",new TagPredicate<IClass>(tg_layer,new String[]{"name","transversal"})));
													addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD INVOKE METHODS ON (4) BASIC JDK CLASSES",new TagPredicate<IClass>(tg_basicJdk)));
													addAssertion(new PredicateAssertion<IClass>("ALL CLASSES TAGGED SERVICE SHOULD INVOKE METHODS ON (5) BASIC APACHE CLASSES",new TagPredicate<IClass>(tg_basicApache)));
												}});
									}});
								}});
							}});
				}});
		// access to fields
				addChildSpec(new Specification<IProject>(new FollowProductionRule<IProject>("Projects"),new NamePredicate<IProject>("petclinic-","tier",""))
						{{
							addChildSpec(new Specification<IClass>(new FollowProductionRule<IClass>("Classes"))
									{{
										addChildSpec(new Specification<IMethod>(new NamePredicate("get","field",""))
												{{
													addAssertion(new FollowAssertion<IMethod>("NO METHOD GET SHOULD MODIFY A FIELD","writesField",null,CardinalityMin._0,CardinalityMax._0));
												}});
									}});
							
						}});
						
	}

}
