package org.jarco.control.specifications.model;

import java.util.ArrayList;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IProject;
import org.jarco.collections.ImmutableList;
import org.jarco.control.specifications.FromXmlFactory;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.IExposableAsANode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OrAssertion<T extends ICodeElement> implements IAssertion<T>, IExposableAsANode {

	@FM(kind=kind.treenode)
	private List<IAssertion<T>> lst=new ArrayList<IAssertion<T>>();
	
	//pour l'éditeur graphique
	public OrAssertion()
	{
		
	}
	
	public ImmutableList<IAssertion<T>> assertions() {
		return new ImmutableList<IAssertion<T>>(lst);
	}

	public void addAssertion(IAssertion<T> a)
	{
		lst.add(a);
	}
	
	public Violation assertRule(T t, List stack) {
		for(IAssertion<T> a : lst)
		{
			Violation v = a.assertRule(t,stack);
			if(v==null)
				return null;
		};
		return new Violation("INTERNAL-ERROR IN OR ASSERTION","No assertion passed in OrAssertion "+lst,t,stack);
	}
	
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("or ");
		for(int i=0;i<lst.size();i++)
		{
			if(i>0)
				sb.append(",");
			sb.append("{"+lst.get(i)+"}");
		}
		return sb.toString();
	}
	
	public String toLabel()
	{
		return "or";
	}
	
	public String toXml()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<or-assertion>");
		for(IAssertion a : lst)
		{
			sb.append(a.toXml());
		}
		sb.append("</or-assertion>");
		return sb.toString();
	}
	
	public static OrAssertion fromXml(FromXmlFactory f, Element e)
	{
		NodeList nl = e.getChildNodes();
		OrAssertion rc = new OrAssertion();
		for (int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
			rc.addAssertion((IAssertion)(f.fromXml((Element)nl.item(i))));
			}
		};
		return rc;
	}

	public void remove(IAssertion assFils) {
		lst.remove(assFils);
	}

}
