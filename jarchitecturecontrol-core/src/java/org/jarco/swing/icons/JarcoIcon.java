package org.jarco.swing.icons;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jarco.code.external.CodeRepositoryModel;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.Violation;
import org.jarco.control.report.genericreport.ReportNode;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.components.JTreeBrowser;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;

public class JarcoIcon {
	  public Icon SPECIFICATION_ICON ;
	  public Image SPECIFICATION_IMAGE ;
	  public Icon CONFIGURATION_ICON ;
	  public Icon CONFIGURATION_SET_ICON ;
	  public Icon MAVEN_REF_ICON ;
	  public Icon VIOLATION_ICON ;
	  public Icon PREDICATE_ICON ;
	  public Icon PRODUCTIONRULE_ICON ;
	  public Icon ASSERTION_ICON ;
	  public Icon CONSEQUENCE_ICON;
	public Icon ASSOCIATION_ICON;
	public Icon TAG_REPOSITORY_ICON;
	public Icon ROLE_TYPE_ICON;
	public Icon TAG_ICON;
	public Icon TAG_ATTRIBUTE_ICON;
	public Icon CODE_REPOSITORY_ICON;
	public Icon CLASS_ICON;
	public Icon FIELD_ICON;
	public Icon METHOD_ICON;
	public Icon PACKAGE_ICON;
	public Icon PROJECT_ICON;

	static JarcoIcon _instance ;
	Component c;
	
	public static void setupSingleton(Component c)
	{
		_instance = new JarcoIcon(c);
	}
	
	public static JarcoIcon instance()
	{
		return _instance;
	}
	
	public JarcoIcon(Component c)
	{
		  try {
			  this.c=c;
			SPECIFICATION_ICON = readIcon("specification.jpg");
			SPECIFICATION_IMAGE = readImage("specification.jpg");
			CONFIGURATION_ICON = readIcon("configuration.jpg");
			CONFIGURATION_SET_ICON = readIcon("configurationset.jpg");
			MAVEN_REF_ICON = readIcon("mavenref2.JPG");
			  VIOLATION_ICON = readIcon("violation.jpg");
			  PREDICATE_ICON = readIcon("predicate.jpg");
			  PRODUCTIONRULE_ICON = readIcon("productionrule.jpg");
			  ASSERTION_ICON = readIcon("assertion.jpg");
			  CONSEQUENCE_ICON = readIcon("consequence.jpg");
			  ASSOCIATION_ICON = readIcon("association.jpg");
			TAG_REPOSITORY_ICON = readIcon("repository.jpg");
			ROLE_TYPE_ICON = readIcon("roletype.jpg");
			TAG_ICON = readIcon("tag.jpg");
			TAG_ATTRIBUTE_ICON = readIcon("tagattribute.jpg");
			CODE_REPOSITORY_ICON = readIcon("coderepository.jpg");
			CLASS_ICON = readIcon("class.jpg");
			FIELD_ICON = readIcon("field.jpg");
			METHOD_ICON = readIcon("method.jpg");
			PACKAGE_ICON = readIcon("package.jpg");
			PROJECT_ICON = readIcon("project.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }

	   Image readImage (String filename) throws IOException
	  {
//		  System.out.println("readImage "+filename);
//		  System.out.println("JarcoIcon.class="+JarcoIcon.class.getResource("JarcoIcon.class"));
//		  System.out.println(filename+"="+JarcoIcon.class.getResource(filename));
		  return ImageIO.read(JarcoIcon.class.getResourceAsStream(filename));
	  }
	  
	   Icon readIcon (String filename) throws IOException
	  {
		  Image img = readImage(filename);
		  int w = img.getWidth(c);
		  int h = img.getHeight(c);
		  return new ImageIcon(img.getScaledInstance(w<24 ? w : 24,h<24 ? h : 24,Image.SCALE_AREA_AVERAGING));
	  }

	public  Icon resolveIconForObject(Object v) {
		Icon icon=null;
		if(v==null)
			icon = SPECIFICATION_ICON;
		else if(v instanceof Specification)
			icon = SPECIFICATION_ICON;
		else if(v instanceof Configuration)
			icon = CONFIGURATION_ICON;
		else if(v instanceof ConfigurationSet)
			icon = CONFIGURATION_SET_ICON;
		else if(v instanceof MavenRef)
			icon = MAVEN_REF_ICON;
		else if(v instanceof Violation)
			icon = VIOLATION_ICON;
		else if(v instanceof IPredicate)
			icon = PREDICATE_ICON;
		else if(v instanceof IProductionRule)
			icon = PRODUCTIONRULE_ICON;
		else if(v instanceof IAssertion)
			icon = ASSERTION_ICON;
		else if(v instanceof IConsequence)
			icon = CONSEQUENCE_ICON;
		else if(v instanceof ITagRepository)
			icon = TAG_REPOSITORY_ICON;
		else if(v instanceof ITagAssociationType)
			icon = ASSOCIATION_ICON;
		else if(v instanceof ITagType)
			icon = TAG_ICON;
		else if(v instanceof ITagAttributeType)
			icon = TAG_ATTRIBUTE_ICON;
		else if(v instanceof ITagRoleType)
			icon = ROLE_TYPE_ICON;
		else if(v instanceof ICodeRepository)
			icon = CODE_REPOSITORY_ICON;
		else if (v instanceof IClass)
			icon = CLASS_ICON;
		else if (v instanceof IMethod)
			icon = METHOD_ICON;
		else if (v instanceof IField)
			icon = FIELD_ICON;
		else if (v instanceof IPackage)
			icon = PACKAGE_ICON;
		else if (v instanceof IProject)
			icon = PROJECT_ICON;
		else if(v instanceof ReportNode)
		{
			ReportNode rn = (ReportNode)v;
			if(rn.getKind().compareTo("violation")==0)
				return VIOLATION_ICON;
			//V0.1 rajouter une icone pour LES violations (se baser sur l'icone pour LA violation)
			return null;
		}
		else if(v instanceof CodeRepositoryModel.CodeElementFacet)
		{
			CodeRepositoryModel.CodeElementFacet f = (CodeRepositoryModel.CodeElementFacet)v;
			//TODO 0.1 customiser une icone par origine (f.getparent) et par règle f.getRule
			return PRODUCTIONRULE_ICON;
		}
		else if(v instanceof CodeRepositoryModel.CodeElementDecorated)
		{
			CodeRepositoryModel.CodeElementDecorated ced = (CodeRepositoryModel.CodeElementDecorated)v;
			return resolveIconForObject(ced.getParent());
		}
		else
			System.err.println("No icon found for "+v.getClass());
		return icon;
	}


}
