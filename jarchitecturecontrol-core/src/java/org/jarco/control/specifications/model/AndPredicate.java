package org.jarco.control.specifications.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.jarco.xml.IPersistableAsXml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AndPredicate<T extends ICodeElement> implements IPredicate<T> {

	private List<IPredicate<T>> _internal;
	
	public AndPredicate()
	{
		_internal=new ArrayList<IPredicate<T>>();
	}
	
	public void add(IPredicate<T> predicate)
	{
		_internal.add(predicate);
	}
	
	@Override
	public ImmutableMap<String, String> include(T t) {
		for(IPredicate<T> p : _internal)
		{
			if(p.include(t)==null)
				return null;
		}
		//TODO 1.1 trouver un moyen de fusionner les réponses pour le AndPredicate
		return new ImmutableMap<String,String>(new HashMap<String,String>());
	}

	public String toString()
	{
		return "And{"+_internal+"}";
	}
	
	@Override
	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<and-predicate>");
		for(IPredicate<T> i : _internal)
		{
			sb.append(i.toXml());
		}
		sb.append("</and-predicate>");
		return sb.toString();
	}

	@Override
	public void fromXml( FromXmlFactory f, Element e)
	{
		NodeList nl = e.getChildNodes();
		for(int i=0;i<nl.getLength();i++)
		{
			Node ni = nl.item(i);
			if(ni instanceof Element)
			{
				IPredicate pi = (IPredicate) f.fromXml((Element)ni);
				this.add(pi);
			}
		}
	}

	@Override
	public String toLabel() {
		StringBuffer sb=new StringBuffer();
		for(IPredicate pi : _internal)
		{
			if(sb.length()>0) sb.append(" <b>AND</b>");
			sb.append(pi.toLabel());
		}
		return sb.toString();
	}
	
	// TODO 0.1 à tester avec l'IHM
}
