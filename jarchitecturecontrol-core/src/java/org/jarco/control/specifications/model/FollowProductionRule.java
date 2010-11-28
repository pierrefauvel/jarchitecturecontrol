package org.jarco.control.specifications.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.ContextStrategies;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FollowProductionRule<T extends ICodeElement> implements IProductionRule<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String name;
	@FM(kind=kind.component)
	private String[] params;
	
	//pour l'éditeur graphique
	public FollowProductionRule()
	{
	}
	
	public FollowProductionRule(String name, String... params)
	{
		this.name=name;
		this.params=params;
	}
	
	private Object invoke (Object aContext, List ctx)
	{
		//todo 0 OPTIMISER : récupéreration de la méthode
		Method m;
		try {
			
			Class[] pt = new Class[params.length];
			for(int i=0;i<pt.length;i++)
				pt[i]=params[i].getClass();
			
			m = aContext.getClass().getMethod("get"+name,pt);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
		Object obj=null;
		try {
			String[] p2 = new String[params.length];
			for(int i=0;i<p2.length;i++)
			{
				p2[i]= ContextStrategies.resolve(params[i],ctx);
				System.out.println("PF75 "+i+":"+ params[i]+" "+p2[i]);
			}
			obj = m.invoke(aContext, p2);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	public List<ElementAndContext<T>> produce(Object aContext, IPredicate<T> filter, List lst) {
		
//		System.out.println("PF51 Looking for "+name+" in "+aContext+" (filtered by "+filter);

		Object obj = invoke(aContext,lst);
		
		if(obj==null)
			throw new RuntimeException("PF105 Null object returned by method "+name+" on "+aContext);
		
		List<ElementAndContext<T>> rc=new ArrayList<ElementAndContext<T>>();
		if(obj instanceof Iterable<?>)
		{
			Iterable<T> l=(Iterable<T>) obj;
			for(T ce:l)
			{
				if(filter==null)
					rc.add(new ElementAndContext<T>(ce));
				else
				{
					ImmutableMap<String,String> fm = filter.include(ce);
					if(fm!=null)
					{
						rc.add(new ElementAndContext<T>(ce,fm));
//						System.out.println("\tPF51 Found "+ce);
					}
					else
					{
//						System.out.println("\t\tPF51 Discarding "+ce);
					}
				}
			}
		}
		else if(obj instanceof ICodeElement)
		{
			rc.add(new ElementAndContext<T>((T)obj));
		}
		else
		{
			System.err.println("PF55 Could not cast "+obj.getClass()+" to iterable nor code element in "+this);
		}
//		System.out.println("PF51 Found "+rc.size());

//		System.out.println("PF56 Called "+m+" on "+aContext+", found "+rc.size());
		
		return rc;
	}
	
	public String toString()
	{
		return "follow production rule name="+name;
	}
	public String toLabel()
	{
		return "<b>name</b>="+name;
	}
	
	public String toXml()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<follow-production-rule name=\""+name+"\">");
		for(String p:params)
			sb.append("<param>"+p+"</param>");
		sb.append("</follow-production-rule>");
		return sb.toString();
	}
	
	public static FollowProductionRule fromXml(FromXmlFactory f, Element e)
	{
		List<String> lst = new ArrayList<String>();
		NodeList nl = e.getChildNodes();
		for(int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
				lst.add( ((Element)(nl.item(i))).getTextContent());
			}
		}
		return new FollowProductionRule(e.getAttribute("name"),lst.toArray(new String[]{}));
	}
}
