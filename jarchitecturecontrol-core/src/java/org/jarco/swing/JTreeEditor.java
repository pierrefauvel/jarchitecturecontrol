package org.jarco.swing;

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
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.jarco.code.external.IProject;
import org.jarco.code.internal.CodeRepositoryInternal;
import org.jarco.code.internal.maven.MavenRef;
import org.jarco.code.internal.maven.MavenRepositorySPI;
import org.jarco.control.report.AnalysisReport;
import org.jarco.control.report.DependenciesReport;
import org.jarco.control.report.IViolationReport;
import org.jarco.control.report.ResultReport;
import org.jarco.control.report.SpecificationReport;
import org.jarco.control.report.TagReport;
import org.jarco.control.report.filesystem.FileSystemViolationReport;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ControlResult;
import org.jarco.control.specifications.FromXmlFactory;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.editor.test.UserObjectModelInterface;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.icons.JarcoIcon;
import org.jarco.tags.external.ITagRepository;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JTreeEditor {
  private static DefaultMutableTreeNode node_root;
  private static DefaultMutableTreeNode node_clipboard;
  private static boolean isCut = false;  

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

  static JFrame f;
  static JSplitPane sp;
  static JTabbedPane tbb;
  
  static void setLeftPane(JComponent cmp)
  {
	  sp.setLeftComponent(cmp);
  }
  
  static void setRightPane(JComponent cmp)
  {
	  sp.setRightComponent(cmp);
  }
  
  static void pack()
  {
//	f.pack();
	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	f.setSize(new Dimension((int)d.getWidth(),(int)d.getHeight()-24));
//	f.invalidate();
  }
  
  private static Icon SPECIFICATION_ICON ;
  private static Icon VIOLATION_ICON ;
  private static Icon PREDICATE_ICON ;
  private static Icon PRODUCTIONRULE_ICON ;
  private static Icon ASSERTION_ICON ;
  private static Icon CONSEQUENCE_ICON;
private static JTable tbl_violations;
  
  static Image readImage (String filename) throws IOException
  {
//	  System.out.println("readImage "+filename);
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
		  VIOLATION_ICON = readIcon("violation.jpg");
		  PREDICATE_ICON = readIcon("predicate.jpg");
		  PRODUCTIONRULE_ICON = readIcon("productionrule.jpg");
		  ASSERTION_ICON = readIcon("assertion.jpg");
		  CONSEQUENCE_ICON = readIcon("consequence.jpg");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  private static class MyTreeCellRenderer extends DefaultTreeCellRenderer {
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
			else
				System.err.println("No icon found for "+v.getClass());

			//CALCUL DE L'ICONE
			if(icon!=null)
				setIcon(icon);
			return this;
		}
	}

  public static void main(final ModelInterface mi, final Map<String,Object> injection, final ITagRepository tr, final Configuration configuration) throws IllegalArgumentException, SecurityException, XPathExpressionException, IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException {
    f = new JFrame("JSpecficiationEditor");
    final JPanel p = (JPanel) f.getContentPane();
    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    
    tbb = new JTabbedPane();
    p.add(tbb);

    tbl_violations = new JTable();
    
    sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	  sp.setAlignmentX(0);
	  sp.setAlignmentY(0);
//    p.add(sp);
	  tbb.add("Specification",sp);
	  tbb.setIconAt(0, SPECIFICATION_ICON);
	tbb.add("Violations",new JScrollPane(tbl_violations));
	tbb.setIconAt(1,VIOLATION_ICON);
    node_root = build(mi,mi.getRoot());
    
    final JTree t = new JTree(node_root);

    t.setCellRenderer(new MyTreeCellRenderer());
    t.setDragEnabled(true);
    t.setTransferHandler(new NodeTransfertHandler(mi));
    setLeftPane(new JScrollPane(t));
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
          pack();
          
        } else {
          System.out.println("No selection");
        }
      }
    });
    final JPopupMenu popup = new JPopupMenu("Titre");
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
	          pack();

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
          pack();

          
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
			try
			{
			long ts = System.currentTimeMillis();
			
			new File("editor").mkdirs();
			
			File old = new File("editor/specification.xml");
			if(old.exists())
				old.renameTo(new File("editor/specification."+ts+".xml"));
			FileWriter fw=new FileWriter(new File("editor/specification.xml"));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(((Specification)node_root.getUserObject()).toXml());
			bw.close();
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
    });
    final JMenuItem mni_restore = popup.add(new AbstractAction("Restore from File"){

		@Override
		public void actionPerformed(ActionEvent e) {
			try
			{
				FromXmlFactory fxf = new FromXmlFactory(tr);
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document d = db.parse(new File("editor/specification.xml"));
				
				Specification s = (Specification) fxf.fromXml(d.getDocumentElement());
				node_root = build(mi,s);
				t.setModel(new DefaultTreeModel(node_root));
				t.invalidate();
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
    });

    //TODO a décommenter une fois que c'est implémenté
    // JConfigurationDialog dlg = new JConfigurationDialog(f);
    // dlg.setVisible(true);
    
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
//		AnalysisReport.report(reports,cr.findProject(mri,true));
	}

    
    final JMenuItem mni_run = popup.add(new AbstractAction("Control specification against code"){
    	public void actionPerformed(ActionEvent evt)
    	{
    		try
    		{
		
		Specification es = (Specification) ((DefaultMutableTreeNode)node_root).getUserObject();
		
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
		DefaultTableModel dtm = new DefaultTableModel(o,h);
		tbl_violations.setModel(dtm);
		tbb.setSelectedIndex(1);
    		}
    		catch(Throwable t)
    		{
    			t.printStackTrace();
    		}
    	}
    	
    });
    
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
    pack();
    f.setVisible(true);
    
  }

}
