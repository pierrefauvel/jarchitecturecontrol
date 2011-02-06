package org.jarco.swing.components;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.control.report.genericreport.FormattedText;
import org.jarco.control.report.genericreport.GenericReport;
import org.jarco.control.report.genericreport.ReportNode;
import org.xml.sax.SAXException;

public class JTreeViewer {
	  private static DefaultMutableTreeNode build ( final ReportNode node)
	  {
		 DefaultMutableTreeNode rc= new DefaultMutableTreeNode(node){
			 @Override
			 public String toString()
			 {
				 if(node==null)
					 return "<html><b>NULL</b></html>";
				 return "<html>"+node.getDescription().getHtml()+"</html>";
			 }
		 };
		 for(ReportNode oi : node.getSubNodes())
		 {
			 rc.add(build(oi));
		 };
		 return rc;
	  }

	  private JSplitPane sp;
	  
	   void setLeftPane(JComponent cmp)
	  {
		  sp.setLeftComponent(cmp);
	  }
	  
	  void setRightPane(JComponent cmp)
	  {
		  sp.setRightComponent(cmp);
	  }
	  private JTree t;
	  private DefaultMutableTreeNode node_root;
	  
	  public JComponent getComponent()
	  {
		  return sp;
	  }
	  
	  public JTreeViewer(GenericReport gr)
	  {	    
	    sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		  sp.setAlignmentX(0);
		  sp.setAlignmentY(0);
	    node_root = build(gr.getRoot());
	    
	    t = new JTree(node_root);
	    t.setCellRenderer(new TreeCellRendererWithIcons());

	    setLeftPane(new JScrollPane(t));
	    t.addTreeSelectionListener(new TreeSelectionListener() {
	      @Override
	      public void valueChanged(TreeSelectionEvent aArg0) {
	        if (aArg0.getNewLeadSelectionPath() != null) {
	          Object o=((DefaultMutableTreeNode)(aArg0.getNewLeadSelectionPath().getLastPathComponent())).getUserObject();
	          if(o!=null)
	          {
	        	  FormattedText detail = ((ReportNode)o).getDetail();
					JEditorPane ep = new JEditorPane("text/html",detail.getHtml());
					setRightPane(ep);
	          }
	          else
	        	  setRightPane(new JPanel());
	          
	        } else {
	        }
	      }
	    });
	  }
}
