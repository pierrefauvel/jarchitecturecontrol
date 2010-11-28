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
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.persistence.SpecificationFromXmlFactory;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.swing.tree.JTreeEditor;
import org.jarco.swing.tree.ModelInterface;
import org.jarco.swing.tree.TreeCellRendererWithIcons;
import org.jarco.tags.external.ITagRepository;
import org.xml.sax.SAXException;

public class JSpecificationEditor {
	
	  static JDialog f;
	  static JTabbedPane tbb;
	  static void pack()
	  {
//		f.pack();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(new Dimension((int)d.getWidth(),(int)d.getHeight()-24));
//		f.invalidate();
	  }
	  
		private static JTable tbl_violations;

	public static void main(JFrame parent, final ModelInterface mi, final Map<String,Object> injection, final ITagRepository tr, final Configuration configuration) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException 
	{
		FromXmlFactory fxf= new SpecificationFromXmlFactory(tr);
		final JTreeEditor jte = new JTreeEditor(mi, injection, "specification", "specification", new JTreeEditor.IContainer() {
			@Override
			public void pack() {
				JSpecificationEditor.pack();
			}
		},fxf);

		f = new JDialog(parent,"Specficiation Editor", true);
	    final JPanel p = (JPanel) f.getContentPane();
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	    
	    tbb = new JTabbedPane();
		  tbb.add("Specification",jte.getPane());
		  tbb.setIconAt(0, JarcoIcon.SPECIFICATION_ICON);
//		    tbl_violations = new JTable();
//		tbb.add("Violations",new JScrollPane(tbl_violations));
//		tbb.setIconAt(1,TreeCellRendererWithIcons.VIOLATION_ICON);
	    p.add(tbb);
//	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
    	final File reports = new File("reports");
		reports.mkdirs();

		final DependenciesReport dr = configuration.getDependenciesReport();
		final MavenRef[] mr = configuration.getMavenComponentReferences();
		MavenRepositorySPI mrspi = new MavenRepositorySPI(dr,configuration.getMavenRepositoryPath(),mr);
		final CodeRepositoryInternal cr=new CodeRepositoryInternal(
				dr,
				configuration.getJDKPath(),
				mrspi
		);
		cr.flush();
		
		System.out.println("Loading projects");
		for(MavenRef mri : mr)
		{
			IProject prj = cr.findProject(mri,true);
			System.out.println("Project "+mri+":"+prj.getClasses().count()+" classes");
//			AnalysisReport.report(reports,cr.findProject(mri,true));
		}
		
/*
		Action control =  new AbstractAction("Control specification against code"){
	    	public void actionPerformed(ActionEvent evt)
	    	{
	    		try
	    		{
			
			Specification es = (Specification) (jte.getRootNode().getUserObject());
			
			System.out.println("[Specification report]");
			es.dump(new SpecificationReport(reports,configuration.getReportPrefix()));
			
			FileWriter fw=new FileWriter(new File("reports/"+configuration.getReportPrefix()+".specification.xml"));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(es.toXml());
			bw.close();
			
			System.out.println("[Control, result & violation reports]");
			ResultReport rr = new ResultReport(reports,configuration.getReportPrefix());
			InMemoryViolationReport vr = new InMemoryViolationReport();
			ControlResult ctr = new ControlResult(rr,vr);
			es.visit(cr, ctr);
			System.out.println("[Tag report]");
			TagReport tgr = new TagReport(reports,configuration.getReportPrefix());
			tr.report(tgr);
			
			dr.close();
			
			Violation[] v = vr.getViolations();
			Object[][] o = new Object[v.length][];
			for(int i=0;i<v.length;i++)
			{
				o[i]=new Object[2];
				o[i][0]=v[i].getId();
				o[i][1]=v[i].getElement().getName();
			};
			
			String[] h = new String[]{"ID", "Element"};
			DefaultTableModel dtm = new DefaultTableModel(o,h)
			{
				public boolean isCellEditable( int row, int col)
				{
					return false;
				}
			};
			tbl_violations.setModel(dtm);
			System.out.println(o.length+" rows");
			tbb.setSelectedIndex(1);
			pack();
	    		}
	    		catch(Throwable t)
	    		{
	    			t.printStackTrace();
	    		}
	    	}
		};
		
		jte.addActionToPopup(control);
*/
		pack();
	    f.setVisible(true);
	}
}
