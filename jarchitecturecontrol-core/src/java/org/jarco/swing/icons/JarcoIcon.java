package org.jarco.swing.icons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;

public class JarcoIcon {
	  public static Icon SPECIFICATION_ICON ;
	  public static Image SPECIFICATION_IMAGE ;
	  public static Icon CONFIGURATION_ICON ;
	  public static Icon CONFIGURATION_SET_ICON ;
	  public static Icon MAVEN_REF_ICON ;
	  public static Icon VIOLATION_ICON ;
	  public static Icon PREDICATE_ICON ;
	  public static Icon PRODUCTIONRULE_ICON ;
	  public static Icon ASSERTION_ICON ;
	  public static Icon CONSEQUENCE_ICON;
	public static Icon ASSOCIATION_ICON;
	public static Icon REPOSITORY_ICON;
	public static Icon ROLE_TYPE_ICON;
	public static Icon TAG_ICON;
	public static Icon TAG_ATTRIBUTE_ICON;

	  static
	  {
		  try {
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
			REPOSITORY_ICON = readIcon("repository.jpg");
			ROLE_TYPE_ICON = readIcon("roletype.jpg");
			TAG_ICON = readIcon("tag.jpg");
			TAG_ATTRIBUTE_ICON = readIcon("tagattribute.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }

	  static Image readImage (String filename) throws IOException
	  {
//		  System.out.println("readImage "+filename);
		  System.out.println("JarcoIcon.class="+JarcoIcon.class.getResource("JarcoIcon.class"));
		  System.out.println(filename+"="+JarcoIcon.class.getResource(filename));
		  return ImageIO.read(JarcoIcon.class.getResourceAsStream(filename));
	  }
	  
	  static Icon readIcon (String filename) throws IOException
	  {
		  //TODO v0.1 revoir le scaling, c'est pas top pour le logo Maven. min(24,dim) ?
		  return new ImageIcon(readImage(filename).getScaledInstance(24,24,Image.SCALE_AREA_AVERAGING));
	  }

	public static Icon resolveIconForObject(Object v) {
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
			icon = REPOSITORY_ICON;
		else if(v instanceof ITagAssociationType)
			icon = ASSOCIATION_ICON;
		else if(v instanceof ITagType)
			icon = TAG_ICON;
		else if(v instanceof ITagAttributeType)
			icon = TAG_ATTRIBUTE_ICON;
		else if(v instanceof ITagRoleType)
			icon = ROLE_TYPE_ICON;
		else
			System.err.println("No icon found for "+v.getClass());
		return icon;
	}


}
