package org.jarco.swing.tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
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

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.configuration.Configuration;
import org.jarco.control.report.AnalysisReport;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.IViolationReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.filesystem.FileSystemViolationReport;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.editor.test.UserObjectModelInterface;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITagRepository;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JTreeEditor {
	
  public static interface IContainer
  {
	  public void pack();
  }
	
  private DefaultMutableTreeNode node_root;
  private DefaultMutableTreeNode node_clipboard;
  private boolean isCut = false;  

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
	 for(Object oi : mi.getChildren(o))
	 {
		 rc.add(build(mi,oi));
	 };
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
  private String directory;
  private String namePrefix;
  private ModelInterface mi;
  private FromXmlFactory fxf;
  
  public JTreeEditor(ModelInterface pmi, final Map<String,Object> injection, String pdirectory, String pnamePrefix, final IContainer container, FromXmlFactory pfxf) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException {
	  this.mi=pmi;
	  this.directory=pdirectory;
	  this.namePrefix=pnamePrefix;
	  this.fxf=pfxf;
    
    sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	  sp.setAlignmentX(0);
	  sp.setAlignmentY(0);
//    p.add(sp);
    node_root = build(mi,mi.getRoot());
    
    t = new JTree(node_root);

    t.setCellRenderer(new TreeCellRendererWithIcons());
    t.setDragEnabled(true);
    t.setTransferHandler(new NodeTransfertHandler(mi));
    setLeftPane(new JScrollPane(t));
    t.addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent aArg0) {
        if (aArg0.getNewLeadSelectionPath() != null) {
          Object o=((DefaultMutableTreeNode)(aArg0.getNewLeadSelectionPath().getLastPathComponent())).getUserObject();
          System.out.println("Selection of " + o);
          if(o!=null)
          {
          JForm form = new JForm(o.getClass(),injection,t);
          form.fromInstance(o);
          setRightPane(form.asPanel());
          }
          else
        	  setRightPane(new JPanel());
          container.pack();
          
        } else {
          System.out.println("No selection");
        }
      }
    });
    final List<JMenuItem> lst_news = new ArrayList<JMenuItem>();
    
    final Map<String,JMenu> hm_groups = new HashMap<String,JMenu>();
    
    for(String ci : mi.getTypes())
    {
    	final String cif = ci;
    	
    	String g = mi.getGroup(cif);

    	
    	AbstractAction a = new AbstractAction("New "+cif) {
	      @Override
	      public void actionPerformed(ActionEvent aArg0) {
	        if (t.getLeadSelectionPath() != null) {
	          System.out.println("New child to " + t.getLeadSelectionPath().getLastPathComponent());
	          DefaultMutableTreeNode parent = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath()
	              .getLastPathComponent();
	          Object uo_fils = mi.newObject(cif);
	          DefaultMutableTreeNode node = new DefaultMutableTreeNode(uo_fils);
	          ((DefaultTreeModel) t.getModel()).insertNodeInto(node, parent, parent.getChildCount());
	          Object uo_parent = (Object)(((DefaultMutableTreeNode)parent).getUserObject());
	          mi.addChild(uo_parent, uo_fils);
	          TreePath tp = new TreePath(node.getPath());
	          t.scrollPathToVisible(tp);
	          
	          Class c = uo_fils.getClass();
	          JForm form = new JForm(uo_fils.getClass(),injection,t);
	          setRightPane(form.asPanel());
	          form.fromInstance(uo_fils);
	          container.pack();

	          t.getSelectionModel().setSelectionPath(tp);
	        }
	      }
	    };
    	
    	if(g!=null)
    	{
    		JMenu group = hm_groups.get(g);
    		if(group==null)
    		{
    			group = new JMenu(g);
    			hm_groups.put(g,group);
    			popup.add(group);
    		};
    		JMenuItem mii=new JMenuItem(a);
			lst_news.add(mii);
			group.add(mii);
    	}
    	else
    	{
    	    lst_news.add(popup.add(a));
    	}
    };
    popup.add(new JSeparator());
    final JMenuItem mni_copy = popup.add(new AbstractAction("Copy") {
      @Override
      public void actionPerformed(ActionEvent aArg0) {
        if (t.getLeadSelectionPath() != null) {
          System.out.println("Copy " + t.getLeadSelectionPath().getLastPathComponent());
          node_clipboard = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath().getLastPathComponent();
          isCut = false;
        }
      }
    });
    final JMenuItem mni_cut = popup.add(new AbstractAction("Cut") {
      @Override
      public void actionPerformed(ActionEvent aArg0) {
        if (t.getLeadSelectionPath() != null) {
          System.out.println("Cut " + t.getLeadSelectionPath().getLastPathComponent());
          node_clipboard = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath().getLastPathComponent();
          isCut = true;
        }
      }
    });
    final JMenuItem mni_paste = popup.add(new AbstractAction("Paste") {
      @Override
      public void actionPerformed(ActionEvent aArg0) {
        if (t.getLeadSelectionPath() != null) {
          System.out.println("Paste " + node_clipboard.getUserObject() + " into "
              + t.getLeadSelectionPath().getLastPathComponent());
          DefaultMutableTreeNode parent = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath()
              .getLastPathComponent();
          Object original = (Object) (node_clipboard.getUserObject());
          Object uo_fils = mi.clone(original);
          DefaultMutableTreeNode node = new DefaultMutableTreeNode(uo_fils);
          ((DefaultTreeModel) t.getModel()).insertNodeInto(node, parent, parent.getChildCount());
          Object uo_parent = (Object)(((DefaultMutableTreeNode)parent).getUserObject());
          mi.addChild(uo_parent,uo_fils);
          t.scrollPathToVisible(new TreePath(node.getPath()));

          Class c = uo_fils.getClass();
          JForm form = new JForm(uo_fils.getClass(),injection,t);
          form.fromInstance(uo_fils);
          setRightPane(form.asPanel());
          container.pack();

          
          if (isCut) {
        	  
        	  DefaultMutableTreeNode ex_parentNode = (DefaultMutableTreeNode) node_clipboard.getParent();
        	  Object ex_uoParent = (Object)(ex_parentNode.getUserObject());
        	  Object ex_uo_fils = (Object) node_clipboard.getUserObject();
        	  mi.removeChild(ex_uoParent, ex_uo_fils);
        	  
            ((DefaultTreeModel) t.getModel()).removeNodeFromParent(node_clipboard);
            node_clipboard = null;
          }
        }
      }
    });
    final JMenuItem mni_deep_paste = popup.add(new AbstractAction("Deep Paste") {
      @Override
      public void actionPerformed(ActionEvent aArg0) {
        if (t.getLeadSelectionPath() != null) {
          System.out.println("Deep Paste " + node_clipboard.getUserObject() + " into "
              + t.getLeadSelectionPath().getLastPathComponent());
          //TODO A Implémenter. Attention au cas où on colle dans le graphe que l'on copie
          //          DefaultMutableTreeNode parent = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath()
          //              .getLastPathComponent();
          //          DefaultMutableTreeNode node = new DefaultMutableTreeNode(node_clipboard.getUserObject());
          //          ((DefaultTreeModel) t.getModel()).insertNodeInto(node, parent, parent.getChildCount());
          //          t.scrollPathToVisible(new TreePath(node.getPath()));
          //          if (isCut) {
          //            ((DefaultTreeModel) t.getModel()).removeNodeFromParent(node_clipboard);
          //            node_clipboard = null;
          //          }
        }
      }
    });
    final JMenuItem mni_delete = popup.add(new AbstractAction("Delete") {
      @Override
      public void actionPerformed(ActionEvent aArg0) {
        if (t.getLeadSelectionPath() != null) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getSelectionModel().getSelectionPath()
              .getLastPathComponent();

    	  Object ex_uoParent = (Object)(((DefaultMutableTreeNode)node.getParent()).getUserObject());
    	  Object ex_uo_fils = (Object) node.getUserObject();
    	  mi.removeChild(ex_uoParent, ex_uo_fils);

          ((DefaultTreeModel) t.getModel()).removeNodeFromParent(node);
//    	  if(sp.getComponentCount()==2)
//        	  sp.remove(1);
          setRightPane(null);
        }
      }
    });
    popup.add(new JSeparator());
    final JMenuItem mni_save = popup.add(new AbstractAction("Save All To File"){

		@Override
		public void actionPerformed(ActionEvent e) {
			saveAllToFile(directory,namePrefix,(IExposableAsANode)node_root.getUserObject());
		}
    });
    final JMenuItem mni_restore = popup.add(new AbstractAction("Restore from File"){

		@Override
		public void actionPerformed(ActionEvent e) {
			restore();
		}
    });

    //TODO a décommenter une fois que c'est implémenté
    // JConfigurationDialog dlg = new JConfigurationDialog(f);
    // dlg.setVisible(true);
    
    
    popup.pack();
    t.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3 && t.getLeadSelectionPath() != null) {
        	Object pere = ((DefaultMutableTreeNode)(t.getLeadSelectionPath().getLastPathComponent())).getUserObject();
          if (t.getLeadSelectionPath().getLastPathComponent().equals(node_root)) {
            for(JMenuItem mni_new : lst_news)
            {
            	mni_new.setEnabled(mi.acceptChildType(pere, mni_new.getText().substring("New ".length())));
            }
            mni_copy.setEnabled(true);
            mni_cut.setEnabled(false);
            mni_paste.setEnabled(true);
            mni_deep_paste.setEnabled(true);
            mni_delete.setEnabled(false);
            mni_save.setEnabled(true);
            mni_restore.setEnabled(true);
          } else {
              for(JMenuItem mni_new : lst_news)
              {
              	mni_new.setEnabled(mi.acceptChildType(pere, mni_new.getText().substring("New ".length())));
              }
            mni_copy.setEnabled(true);
            mni_cut.setEnabled(true);
            mni_paste.setEnabled(true);
            mni_deep_paste.setEnabled(true);
            mni_delete.setEnabled(true);
            mni_save.setEnabled(true);
            mni_restore.setEnabled(true);
          }
          popup.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    });
    
  }

public static void saveAllToFile(String pdirectory, String pnamePrefix, IExposableAsANode eaan) {
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
	bw.write(eaan.toXml());
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

public void addActionToPopup(Action control) {
	popup.add(control);
}

public void restore() {
	try
	{				
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		String name = directory + "/" + namePrefix + ".xml";
		Document d = db.parse(new File(name));
		
		Object s = (Object) fxf.fromXml(d.getDocumentElement());
		mi.setRoot(s);
		node_root = build(mi,s);
		t.setModel(new DefaultTreeModel(node_root));
		t.invalidate();
	}
	catch(Throwable t)
	{
		t.printStackTrace();
	}
}

}
