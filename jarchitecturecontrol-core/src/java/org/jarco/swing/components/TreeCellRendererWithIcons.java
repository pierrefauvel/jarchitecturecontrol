package org.jarco.swing.components;

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
import org.jarco.control.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITag;
import org.jarco.tags.external.ITagAssociation;
import org.jarco.tags.external.ITagAssociationType;
import org.jarco.tags.external.ITagAttributeType;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagRoleType;
import org.jarco.tags.external.ITagType;

public class TreeCellRendererWithIcons extends DefaultTreeCellRenderer {

	  
	  
	  
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
														  boolean expanded, boolean leaf, int row, boolean hasFocus) {
				Component c=super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				if (!(value instanceof DefaultMutableTreeNode)) return this;
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
				Icon icon=null;
				
				Object v = ((DefaultMutableTreeNode)value).getUserObject();
				icon = JarcoIcon.instance().resolveIconForObject(v);
				//CALCUL DE L'ICONE
				if(icon!=null)
					setIcon(icon);
				return this;
			}
}
