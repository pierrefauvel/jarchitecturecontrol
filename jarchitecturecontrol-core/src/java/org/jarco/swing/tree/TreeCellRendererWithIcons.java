package org.jarco.swing.tree;

import java.awt.Component;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jarco.code.internal.maven.MavenRef;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationSet;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITagRepository;

public class TreeCellRendererWithIcons extends DefaultTreeCellRenderer {

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
	  public static Icon TAG_REPOSITORY_ICON;
	  
	  static Image readImage (String filename) throws IOException
	  {
//		  System.out.println("readImage "+filename);
		  return ImageIO.read(JarcoIcon.class.getResourceAsStream(filename));
	  }
	  
	  static Icon readIcon (String filename) throws IOException
	  {
		  return new ImageIcon(readImage(filename).getScaledInstance(24,24,Image.SCALE_AREA_AVERAGING));
	  }
	  
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
			  TAG_REPOSITORY_ICON = readIcon("consequence.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
														  boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				if (!(value instanceof DefaultMutableTreeNode)) return this;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
				Icon icon=null;
				
				Object v = ((DefaultMutableTreeNode)value).getUserObject();
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
				else
					System.err.println("No icon found for "+v.getClass());

				//CALCUL DE L'ICONE
				if(icon!=null)
					setIcon(icon);
				return this;
			}
}
