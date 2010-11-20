package org.jarco.control.specifications.model;

import java.util.List;

import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PredicateAssertion<T extends ICodeElement> implements IAssertion<T>, IExposableAsANode {

	@FM(kind=kind.treenode)
	private IPredicate<T> f;
	@FM(kind=kind.component)
	private String id;
	
	//pour l'éditeur graphique
	public PredicateAssertion()
	{
		
	}
	
	public PredicateAssertion(String id,IPredicate<T> f)
	{
		this.id=id;
		this.f=f;
	}
	
	@Override
	public Violation assertRule(T t, List stack) {
		if(f.include(t)==null)
			return new Violation(id,"FilterAssertion "+f+" is violated",t,stack);
		return null;
	}

	public String toString()
	{
		return "["+id+"] check that {"+f+"}";
	}
	
	public String toLabel()
	{
		return "[<b>"+id+"</b>] assert predicate is true";
	}
	
	public String toXml()
	{
		return "<predicate-assertion id=\""+id+"\">" + f.toXml() + "</predicate-assertion>";
	}
	
	public static PredicateAssertion fromXml(FromXmlFactory f, Element e)
	{
		String id = e.getAttribute("id");
		NodeList nl = e.getChildNodes();
		for (int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
			return new PredicateAssertion(id,(IPredicate)f.fromXml((Element)(e.getChildNodes().item(i))));
			}
		};
		throw new RuntimeException("Could not find predicate child in predicate assertion "+f.dump(e));
	}

	public void setPredicate(IPredicate f) {
		this.f=f;
	}

	public IPredicate predicate() {
		return f;
	}
}
