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

import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.configuration.ConfigurationsModel;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.tree.JTreeEditor;
import org.jarco.swing.tree.ModelInterface;
import org.jarco.swing.tree.TreeCellRendererWithIcons;
import org.jarco.tags.external.ITagRepository;
import org.jarco.xml.ConfigurationFromXmlFactory;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.SpecificationFromXmlFactory;
import org.xml.sax.SAXException;

public class JConfigurationEditor {
	
	  static JDialog f;
	  static JTabbedPane tbb;
	  static void pack()
	  {
//		f.pack();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(new Dimension((int)d.getWidth(),(int)d.getHeight()-24));
//		f.invalidate();
	  }
	  
	public static void main(final JFrame parent, final ModelInterface mi, final Map<String,Object> injection) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException 
	{
		FromXmlFactory fxf= new ConfigurationFromXmlFactory();
		final JTreeEditor jte = new JTreeEditor(mi, injection, "configuration", "configuration", new JTreeEditor.IContainer() {
			@Override
			public void pack() {
				JConfigurationEditor.pack();
			}
		},fxf);
		
		//TODO V1.0 masquer l'action si on n'est pas sur une configuration
		AbstractAction selection = new AbstractAction("Select configuration")
		{
			public void actionPerformed(ActionEvent e)
			{
				DefaultMutableTreeNode selectedNode = jte.getSelectedNode();
				Object uo = selectedNode.getUserObject();
				if(uo instanceof Configuration)
				{
					((ConfigurationsModel)mi).setSelectedConfiguration((Configuration)uo);
				}
			}
		};
		
		
		jte.addActionToPopup(selection);
		
		f = new JDialog(parent,"Configuration Editor",true);
	    final JPanel p = (JPanel) f.getContentPane();
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	    
	    tbb = new JTabbedPane();
		  tbb.add("Configuration",jte.getPane());
		  tbb.setIconAt(0, TreeCellRendererWithIcons.CONFIGURATION_ICON);
	    p.add(tbb);
//	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
    	final File configuration = new File("configuration");
		configuration.mkdirs();
		
		jte.restore();
		
	    pack();
	    f.setVisible(true);
	}
}
