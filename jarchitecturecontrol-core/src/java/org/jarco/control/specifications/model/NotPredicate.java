package org.jarco.control.specifications.model;

import java.util.HashMap;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NotPredicate<T extends ICodeElement> implements IPredicate<T> {

	private IPredicate<T> _internal;
	
	public NotPredicate(IPredicate<T> predicate)
	{
		_internal=predicate;
	}
	
	@Override
	public ImmutableMap<String, String> include(T t) {
		if(_internal.include(t)!=null)
			return null;
		return new ImmutableMap<String,String>(new HashMap<String,String>());
	}

	@Override
	public String toXml() {
		// TODO 0.1 à implémenter
		return null;
	}
 
	@Override
	public void fromXml( FromXmlFactory f, Element e)
	{
		NodeList nl = e.getChildNodes();
		boolean found=false;
		for(int i=0;i<nl.getLength();i++)
		{
			Node ni = nl.item(i);
			if(ni instanceof Element )
			{
				if(!found)
				{
					IPredicate pi = (IPredicate) f.fromXml((Element)ni);
					this._internal=pi;
					found = true;
				}
				else
				{
					throw new RuntimeException("Multiple IPredicate child nodes in not-predicate");
				}
			}
		}
		if(!found)
			throw new RuntimeException("No IPredicate child nodes in not-predicate");
	}
	// TODO 0.1 à tester avec l'IHM

	@Override
	public String toLabel() {
		return "<b>NOT {</b>"+_internal.toLabel()+"}";
	}
}
