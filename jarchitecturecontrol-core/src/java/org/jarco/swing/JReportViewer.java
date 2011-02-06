package org.jarco.swing;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jarco.control.report.ViolationGReport;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.swing.components.JTreeViewer;
import org.jarco.swing.icons.JarcoIcon;

public class JReportViewer {

	
	public static void main(JFrame parent,GenericReport gr, String name)
	{
//		JDialog f = new JDialog(parent,"Report viewer",true);
		JFrame f = new JFrame(name+" viewer");
	    final JPanel p = (JPanel) f.getContentPane();
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	    
	    JTabbedPane tbb = new JTabbedPane();
		  tbb.add(name,new JTreeViewer(gr).getComponent());
		  tbb.setIconAt(0, JarcoIcon.instance().VIOLATION_ICON);
	    p.add(tbb);
	    
	    f.pack();
	    f.setVisible(true);
	}
}
