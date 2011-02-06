package org.jarco.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.CodeRepositoryModel;
import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationsModel;
import org.jarco.control.Violation;
import org.jarco.control.report.DependenciesGReport;
import org.jarco.control.report.ControlGReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagGReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.components.JTreeBrowser;
import org.jarco.swing.components.JTreeEditor;
import org.jarco.swing.components.ModelInterface;
import org.jarco.swing.components.TreeCellRendererWithIcons;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.internal.TagRepositoryModel;
import org.jarco.xml.ConfigurationFromXmlFactory;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.SpecificationFromXmlFactory;
import org.jarco.xml.TagRepositoryFromXmlFactory;
import org.xml.sax.SAXException;

public class JCodeRepositoryBrowser {
	
	  static JDialog f;
	  static JTabbedPane tbb;
	  static void pack()
	  {
//		f.pack();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(new Dimension((int)d.getWidth(),(int)d.getHeight()-24));
//		f.invalidate();
	  }
	  
	public static void main(JFrame parent, final CodeRepositoryModel ci) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException 
	{
		final JTreeBrowser jte = new JTreeBrowser(ci, new JTreeBrowser.IContainer() {
			@Override
			public void pack() {
				JCodeRepositoryBrowser.pack();
			}
		});
				
		f = new JDialog(parent,"Code Repository Browser",true);
	    final JPanel p = (JPanel) f.getContentPane();
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	    
	    tbb = new JTabbedPane();
		  tbb.add("Tag Repository",jte.getPane());
		  tbb.setIconAt(0, JarcoIcon.instance().TAG_REPOSITORY_ICON);
	    p.add(tbb);
	    
	    pack();
	    f.setVisible(true);
	}
}
