package org.jarco.swing.tree;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jarco.code.external.EModifier;
import org.jarco.control.specifications.model.FM;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.tags.external.ITagRepository;
import org.jarco.tags.external.ITagType;
import org.jarco.tags.internal.TagRepositoryModel;

public class JForm {
	
	enum mappings {string, arrayOfString, arrayOfPairsOfString, _enum, tagtypes, arrayOfModifiers};
	
	private Class c;
	private List<Field> f;
	private Map<String,Editor> m2;
	private JPanel pnl;
	
	private Map<String,Object> injection;

	private Object instance;
	
	private static class Editor
	{
		JComponent cmp;
		mappings mapping;
		
		public String toString()
		{
			return cmp.getClass().getSimpleName()+"-" + mapping.toString();
		}
	}
	
	public JForm(Class c, Map<String,Object> injection, final JComponent t)
	{
		this.c=c;
		f=new ArrayList<Field>();
		m2=new HashMap<String,Editor>();
		this.injection=injection;
		pnl=new JPanel();
		pnl.setBorder(new TitledBorder(c.getSimpleName()));
		BoxLayout bl = new BoxLayout(pnl,BoxLayout.Y_AXIS);
		pnl.setLayout(bl);
		pnl.setAlignmentX(0);
		pnl.setAlignmentY(0);
		System.out.println("PF101["+c.getName()+"]");
		Field[] f = c.getDeclaredFields();
		for (Field fi : f)
		{
			if((fi.getModifiers() & Modifier.STATIC)!=0)
				continue;
			if(fi.isSynthetic())
				continue;
			if(injection.containsKey(fi.getType().getName()))
				continue;
			FM fm = (FM)(fi.getAnnotation(FM.class));
			if(fm==null)
				; //PF ignorer ? ex: ConfigurationSet lst
			else if(fm.kind()==kind.component)
			{
				addField(fi);
			};
		}
		if(!c.getSuperclass().equals(Object.class))
		{
			f = c.getSuperclass().getDeclaredFields();
			for (Field fi : f)
			{
				if((fi.getModifiers() & Modifier.STATIC)!=0)
					continue;
				if(fi.isSynthetic())
					continue;
				if(injection.containsKey(fi.getType().getName()))
					continue;
				if(((FM)(fi.getAnnotation(FM.class))).kind()==kind.component)
				{
					addField(fi);
				};
			}
		};
		pnl.add(new JButton(new AbstractAction("Save"){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				toInstance();
				t.revalidate();
			}
			
		}));
	}
	public void addField(Field fi)
	{
		try
		{
		fi.setAccessible(true);
		f.add(fi);
		
		Editor editor = makeupEditorForField(fi.getType(),fi.getName());
		m2.put(fi.getName(),editor);
		JPanel pnl2 = new JPanel();
		pnl2.setLayout(new BoxLayout(pnl2,BoxLayout.X_AXIS));
		pnl.add(pnl2);
		JLabel lbl =new JLabel(fi.getName()+":");
		lbl.setMaximumSize(new Dimension(150,24));
		pnl2.add(lbl);
		pnl2.add(editor.cmp);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public JPanel asPanel()
	{
		return pnl;
	}
	private Editor makeupEditorForField(Class<?> type, String fn) {
		if(String.class.isAssignableFrom(type))
		{
			Editor ed = new Editor();
			JTextField txf = new JTextField();
			txf.setMaximumSize(new Dimension(200,24));
			ed.cmp=txf;
			ed.mapping = mappings.string;
			return ed;
		}
		//TODO v1.1 en cas de Array, JTable + maître/détail avec ... un sous JForm
		else if(String[].class.isAssignableFrom(type))
		{
			Editor ed = new Editor();
			JTextField txf = new JTextField();
			txf.setMaximumSize(new Dimension(200,24));
			ed.cmp=txf;
			ed.mapping = mappings.arrayOfString;
			return ed;
		}
		else if(String[][].class.isAssignableFrom(type))
		{
			Editor ed = new Editor();
			JTextField txf = new JTextField();
			txf.setMaximumSize(new Dimension(200,24));
			ed.cmp=txf;
			ed.mapping = mappings.arrayOfPairsOfString;
			return ed;
		}
		else if(type.isEnum())
		{
			System.out.println("PF102 "+type.getName());
			Editor ed = new Editor();
			Object[] o = type.getEnumConstants();
			JComboBox cmb = new JComboBox(o);
			cmb.setMaximumSize(new Dimension(200,24));
			ed.cmp=cmb;
			ed.mapping = mappings._enum;
			return ed;
		}
		else if(ITagType.class.isAssignableFrom(type))
		{
			Editor ed = new Editor();
			ITagRepository repo = (ITagRepository) ((TagRepositoryModel)(injection.get(TagRepositoryModel.class.getName()))).getRoot();
			List lst = new ArrayList();
			for(Object o:repo.getTagTypes())
				lst.add(o);
			JComboBox cmb=new JComboBox(lst.toArray());
			cmb.setMaximumSize(new Dimension(200,24));
			ed.cmp=cmb;
			ed.mapping = mappings.tagtypes;
			return ed;
		}
		else if(EModifier[].class.isAssignableFrom(type))
		{
			Editor ed = new Editor();
			Object[] o = EModifier.values();
			JList lst = new JList(o);
			lst.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			lst.setMaximumSize(new Dimension(200,24*o.length));
			ed.cmp=lst;
			ed.mapping = mappings.arrayOfModifiers;
			return ed;
		}
		else
			throw new UnsupportedOperationException("Type "+type+" for field "+fn+" "+"in "+c+" is not yet supported");
	}
	public void toInstance()
	{
		try
		{
			Object o = instance;
			for(Field fi:f)
			{
				if(((FM)(fi.getAnnotation(FM.class))).kind()!=kind.ignore)
				{
					fi.set(o, getValueByName(fi.getName()));
				}
				else
					fi.set(o, null);
			}
			for (Field fi : f)
			{
				if(((FM)(fi.getAnnotation(FM.class))).kind()==kind.injected)
					fi.set(o,injection.get(fi.getType().getName()));
			}
			if(!c.getSuperclass().equals(Object.class))
			{
				for (Field fi : c.getSuperclass().getDeclaredFields())
				{
					if(((FM)(fi.getAnnotation(FM.class))).kind()==kind.injected)
						fi.set(o,injection.get(fi.getType().getName()));
				}
			};
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	public void fromInstance(Object o)
	{
		try
		{
			for(Field fi:f)
			{
				setValueByName(fi.getName(),fi.get(o));
			}
			instance = o;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	public Object getValueByName(String fn)
	{
		Editor editor = m2.get(fn);
		if(editor==null)
			throw new RuntimeException("Could not find component "+fn);
		if(editor.cmp instanceof JTextField && editor.mapping==mappings.string)
		{
			return ((JTextField) editor.cmp).getText();
		}
		if(editor.cmp instanceof JTextField && editor.mapping==mappings.arrayOfString)
		{
			String txt = ((JTextField) (editor.cmp)).getText();
			if(txt.indexOf(",")==-1)
			{
				if(txt.trim().length()==0) return new String[]{};
				return new String[]{txt};
			}
			return txt.split(",");
		}
		if(editor.cmp instanceof JTextField && editor.mapping==mappings.arrayOfPairsOfString)
		{
			String txt = ((JTextField) (editor.cmp)).getText();
			String[] txt1 = null;
			if(txt.indexOf(",")==-1)
			{
				if(txt.trim().length()==0) 
					txt1=new String[]{};
				else
					txt1= new String[]{txt};
			}
			else
				txt1=txt.split(",");
			String[][] rc = new String[txt1.length][];
			for (int i=0;i<rc.length;i++)
			{
				rc[i]=new String[2];
				int idx = txt1[i].indexOf("=");
				if(idx==-1)
				{
					rc[i][0]=txt1[i];
					rc[i][1]="";
				}
				else
				{
					rc[i][0]=txt1[i].substring(0,idx);
					rc[i][1]=txt1[i].substring(idx+1);
				}
			};
			return rc;
		}
		if(editor.cmp instanceof JComboBox && editor.mapping == mappings._enum)
		{
			JComboBox cmb = (JComboBox)(editor.cmp);
			return cmb.getSelectedObjects()[0];
		}
		if(editor.cmp instanceof JComboBox && editor.mapping == mappings.tagtypes)
		{
			JComboBox cmb = (JComboBox)(editor.cmp);
			return cmb.getSelectedObjects()[0];
		}
		if(editor.cmp instanceof JList && editor.mapping == mappings.arrayOfModifiers)
		{
			JList lst = (JList)(editor.cmp);
			Object[] obj = lst.getSelectedValues();
			List<EModifier> rc = new ArrayList<EModifier>();
			for(Object obji:obj)
				rc.add((EModifier)obji);
			return rc.toArray(new EModifier[]{});
		}
		throw new UnsupportedOperationException("Component "+editor+" is not supported");
	}
	public void setValueByName(String fn, Object o)
	{
		Editor editor = m2.get(fn);
		if(editor==null)
			throw new RuntimeException("Could not find component "+fn);
		if(editor.cmp instanceof JTextField && editor.mapping == mappings.string)
		{
			((JTextField)editor.cmp).setText((String)(o));
			return;
		}
		if(editor.cmp instanceof JTextField && editor.mapping == mappings.arrayOfString)
		{
			String[] a = (String[])o;
			StringBuffer sb=new StringBuffer();
			if(a!=null && a.length>0)
			{
				sb.append(a[0]);
				for(int i=1;i<a.length;i++)
					sb.append(","+a[i]);
			};
			((JTextField)editor.cmp).setText(sb.toString());
			return;
		}
		if(editor.cmp instanceof JTextField && editor.mapping == mappings.arrayOfPairsOfString)
		{
			String[][] a = (String[][])o;
			StringBuffer sb=new StringBuffer();
			if(a!=null && a.length>0)
			{
				sb.append(a[0][0]+"="+a[0][1]);
				for(int i=1;i<a.length;i++)
					sb.append(","+a[i][0]+"="+a[i][1]);
			};
			((JTextField)editor.cmp).setText(sb.toString());
			return;
		}
		if(editor.cmp instanceof JComboBox && editor.mapping == mappings._enum)
		{
			JComboBox cmb = (JComboBox)(editor.cmp);
			cmb.getModel().setSelectedItem(o);
			return;
		}
		if(editor.cmp instanceof JComboBox && editor.mapping == mappings.tagtypes)
		{
			JComboBox cmb = (JComboBox)(editor.cmp);
			cmb.getModel().setSelectedItem(o);
			return;
		}
		if(editor.cmp instanceof JList && editor.mapping == mappings.arrayOfModifiers)
		{
			JList lst = (JList)(editor.cmp);
			EModifier[] m = (EModifier[])o;
			int[] idx = new int[m.length];
			main_loop:for (int i=0;i<idx.length;i++)
			{
				for(int j=0;j<lst.getModel().getSize();j++)
				{
					if (lst.getModel().getElementAt(j)==m[i])
					{
						idx[i]=j;
						continue main_loop;
					}	
				}
			}
			lst.setSelectedIndices(idx);
			return;
		}
		else throw new UnsupportedOperationException("Component "+editor+" is not supported");
	}
}
