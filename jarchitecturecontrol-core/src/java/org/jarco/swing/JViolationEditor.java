package org.jarco.swing;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.report.memory.InMemoryViolationReport;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.model.Specification;
import org.jarco.swing.tree.IExposableAsANode;

public class JViolationEditor extends JDialog {
	
	private JLabel lbl_id=new JLabel("<id>");
	private JLabel lbl_message=new JLabel("<message>");
	private JTable tbl_stack;
	private JTable tbl_violations;
	
	public JViolationEditor(JFrame f, InMemoryViolationReport vr)
	{
		super(f,"Control Result", true);
		final Violation[] v = vr.getViolations();
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

		
		JPanel pnl_detail = new JPanel();
		pnl_detail.setLayout(new BoxLayout(pnl_detail,BoxLayout.Y_AXIS));
		{
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row,BoxLayout.X_AXIS));
			row.add(new JLabel("Id: "));
			row.add(lbl_id);
			pnl_detail.add(row);
		}
		pnl_detail.setLayout(new BoxLayout(pnl_detail,BoxLayout.Y_AXIS));
		{
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row,BoxLayout.X_AXIS));
			row.add(new JLabel("Message: "));
			row.add(lbl_message);
			pnl_detail.add(row);
		}
		Object[][] o1 = new Object[1][2];
		o1[0][0]="-";
		o1[0][1]="-";
		String[] h1 = new String[]{"Type", "Description"};
		DefaultTableModel dtm1 = new DefaultTableModel(o1,h1)
		{
			public boolean isCellEditable( int row, int col)
			{
				return false;
			}
		};
	    tbl_stack = new JTable(dtm1);
		pnl_detail.add(new JScrollPane(tbl_stack));
		
	    JTabbedPane tbb = new JTabbedPane();
	    
	    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    
	    tbl_violations = new JTable();
		tbb.add("Violations",new JScrollPane(tbl_violations));
		tbl_violations.setModel(dtm);
		sp.setLeftComponent(new JScrollPane(tbl_violations));
		sp.setRightComponent(pnl_detail);
				
		tbl_violations.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0) {
				int row = tbl_violations.getSelectedRow();
				Violation vi = v[row];
				lbl_id.setText(vi.getId());
				lbl_message.setText(vi.getMessage());
				
				List<Object> stack = vi.getStack(); 
				Object[][] o = new Object[stack.size()][];
				for(int i=0;i<o.length;i++)
				{
					o[i]=new Object[2];
					Object obj = stack.get(i);
					if(obj instanceof Specification)
					{
						Specification s=(Specification)(stack.get(i));
						o[i][0]=s.getClass().getSimpleName();
						o[i][1]=s;
					}
					else
					{
						ElementAndContext e = (ElementAndContext)(stack.get(i));
						ICodeElement ce = e.getElement();
						//TODO v1.0 cloner le code element lors de l'ajout à la Violation, afin d'avoir la valeur des propriétés du context au moment de la violation
						o[i][0]=ce.getClass().getSimpleName();
						o[i][1]=ce.getName();
					}
				};
				String[] h = new String[]{"Type", "Description"};
				DefaultTableModel dtm = new DefaultTableModel(o,h)
				{
					public boolean isCellEditable( int row, int col)
					{
						return false;
					}
				};
				tbl_stack.setModel(dtm);
				tbl_stack.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer(){

					public Component getTableCellRendererComponent(JTable arg0,
							Object arg1, boolean selected, boolean arg3, int arg4, int arg5) {
						final JLabel lbl = new JLabel();
						lbl.setOpaque(true);
						if(selected)
						{
							lbl.setBackground(arg0.getSelectionBackground());
							lbl.setForeground(arg0.getSelectionForeground());
						}
						else
						{
							lbl.setBackground(arg0.getBackground());
							lbl.setForeground(arg0.getForeground());
						}
						if(arg1 instanceof IExposableAsANode)
						{
							lbl.setText("<html>"+((IExposableAsANode)arg1).toLabel()+"</html>");
						}
						else
						{
							lbl.setText(arg1.toString());
						}
						return lbl;
					}
					
				});
			}
		});
		
		System.out.println(o.length+" rows");
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		getContentPane().add(sp);
		pack();
		setVisible(true);
	}

}
