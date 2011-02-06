package org.jarco.swing.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.CodeRepositoryModel.CodeElementDecorated;
import org.jarco.code.external.CodeRepositoryModel.CodeElementFacet;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.IPersistableAsXml;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JTreeBrowser {
	
  public static interface IContainer
  {
	  public void pack();
  }
	
  private DefaultMutableTreeNode node_root;

  private static DefaultMutableTreeNode build ( ModelInterface mi, final Object o)
  {
	 DefaultMutableTreeNode rc= new DefaultMutableTreeNode(o){
		 @Override
		 public String toString()
		 {
			 if(o==null)
				 return "<html><b>NULL</b></html>";
			 if(! (o instanceof IExposableAsANode))
				 return o.toString();
			 return "<html>"+((IExposableAsANode)o).toLabel()+"</html>";
		 }
	 };
	 if(o instanceof CodeElementDecorated)
	 {
			for(Object child:mi.getChildren(o))
			{
				rc.add(build(mi,child));
			}
	 }
	 return rc;
  }

  private JSplitPane sp;
  private JPopupMenu popup = new JPopupMenu("Titre");
  
   void setLeftPane(JComponent cmp)
  {
	  sp.setLeftComponent(cmp);
  }
  
  void setRightPane(JComponent cmp)
  {
	  sp.setRightComponent(cmp);
  }
  private JTree t;
  private ModelInterface mi;
  
  public JTreeBrowser(ModelInterface pmi, final IContainer container) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException {
	  this.mi=pmi;
    
    sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	  sp.setAlignmentX(0);
	  sp.setAlignmentY(0);
//    p.add(sp);
    node_root = build(mi,mi.getRoot());
    
    t = new JTree(node_root);
/*
    t.addTreeWillExpandListener(new TreeWillExpandListener(){
		@Override
		public void treeWillCollapse(TreeExpansionEvent arg0)
				throws ExpandVetoException {
			//do nothing
		}
		@Override
		public void treeWillExpand(TreeExpansionEvent arg0)
				throws ExpandVetoException {
			DefaultMutableTreeNode current = (DefaultMutableTreeNode)(arg0.getPath().getLastPathComponent());
			Object o=current.getUserObject();
			System.out.println("treeWillExpand "+o);
			if(o instanceof CodeElementFacet)
			{
				for(Object child:mi.getChildren(o))
				{
					current.add(build(mi,child));
				}
			}
		}
    	
    });
*/
    
    t.setCellRenderer(new TreeCellRendererWithIcons());
    setLeftPane(new JScrollPane(t));
    t.addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent aArg0) {
        if (aArg0.getNewLeadSelectionPath() != null) {
          Object o=((DefaultMutableTreeNode)(aArg0.getNewLeadSelectionPath().getLastPathComponent())).getUserObject();

          if(o instanceof CodeElementFacet)
          {
        	  System.out.println("Selection of " + ((CodeElementFacet)o).getParent().getName());
          }
          else if(o instanceof CodeElementDecorated)
          {
        	  System.out.println("Selection of " + ((CodeElementDecorated)o).getParent().getName());
          }
          else
        	  System.out.println("Selection of " + o);
          if(o!=null)
          {
        	  JPanel p=new JPanel();
        	  //TODO 0.1 à récupérer du viewer
        	  p.add(new JLabel(o.toString()));
          setRightPane(p);
          }
          else
        	  setRightPane(new JPanel());
//          container.pack();

          DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)((DefaultMutableTreeNode)(aArg0.getNewLeadSelectionPath().getLastPathComponent()));
          Object userObject = currentNode.getUserObject();
          
			if(o instanceof CodeElementFacet && currentNode.getChildCount()==0)
			{
				for(Object child:mi.getChildren(userObject))
				{
					currentNode.add(build(mi,child));
				}
			}

          
        } else {
          System.out.println("No selection");
        }
      }
    });
    
  }

public static void saveAllToFile(String pdirectory, String pnamePrefix, IPersistableAsXml iPersistableAsXml) {
	try
	{
	long ts = System.currentTimeMillis();
	
	new File(pdirectory).mkdirs();
	
	String nameForNew = pdirectory + "/" + pnamePrefix + ".xml";
	String nameForOld = pdirectory + "/" + pnamePrefix + "."+ts+".xml";
	
	File old = new File(nameForNew);
	if(old.exists())
		old.renameTo(new File(nameForOld));
	FileWriter fw=new FileWriter(new File(nameForNew));
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(iPersistableAsXml.toXml());
	bw.close();
	}
	catch(Throwable t)
	{
		t.printStackTrace();
	}
}

public Component getPane() {
	return sp;
}

public DefaultMutableTreeNode getRootNode() {
	return node_root;
}

public DefaultMutableTreeNode getSelectedNode(){
	return (DefaultMutableTreeNode)(t.getLeadSelectionPath().getLastPathComponent());
}


}
