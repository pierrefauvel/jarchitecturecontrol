package org.jarco.control.specifications.model;

import java.util.Collection;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.Violation;
import org.jarco.control.specifications.itf.IAssertion;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FollowAssertion<T extends ICodeElement> implements IAssertion<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String id;
	@FM(kind=kind.component)
	private String n;
	@FM(kind=kind.treenode)
	private IPredicate<T> f;
	public enum CardinalityMin { _0, _1 };
	@FM(kind=kind.component)
	private CardinalityMin min;
	public enum CardinalityMax { _0, _1, _N };
	@FM(kind=kind.component)
	private CardinalityMax max;
	
	//pour l'éditeur graphique
	public FollowAssertion()
	{
	}
	
	public FollowAssertion(String id,String n, IPredicate<T> f, CardinalityMin min, CardinalityMax max)
	{
		this.id=id;
		this.n=n;
		this.f=f;
		this.min=min;
		this.max=max;
	}
	
	public String getId()
	{
		return id;
	}
	
	public Violation assertRule(T t, List stack) {
		IProductionRule fpr=new FollowProductionRule<T>(n);		
		Collection<ElementAndContext<T>> c = fpr.produce(t, f,stack);
		int size = c.size();
		switch(min)
		{
			case _0:{
				//
				break;
			}
			case _1:{
				if(size==0)
				{
					String msg = "FollowAssertion {"+this+"} failed, expected at least one, found "+size;
					return new Violation(id,msg,t,stack);
				}
				break;
			}
		}
		switch(max)
		{
			case _0:{
				//
				if(size>0)
				{
					String msg = "FollowAssertion  {"+this+"} failed, expected none, found "+size;
					return new Violation(id,msg,t,stack);
				}
				break;
			}
			case _1:{
				//
				if(size>1)
				{
					String msg = "FollowAssertion  {"+this+"} failed, expected at most one, found "+size;
					return new Violation(id,msg,t,stack);
				}
				break;
			}
			case _N:{
				//
			}
		};
		return null;
	}

	public String toString()
	{
		return "["+id+"]"+"production rule name="+n+",filter={"+f+"}, count should be in ["+min+","+max+"]";
	}

	public String toLabel()
	{
		return "[<b>"+id+"</b>]{<b>productionRule</b>="+n+",<b>filter</b>="+f+"}.count in ["+min+","+max+"]";
	}
	
	public String toXml()
	{
		return "<follow-assertion id=\""+id+"\" name=\""+n+"\" min=\""+min+"\" max=\""+max+"\">"+ (f!=null ? f.toXml():"")+"</follow-assertion>";
	}
	
	public static FollowAssertion fromXml(FromXmlFactory f, Element e)
	{
		String n = e.getAttribute("name");
		String id = e.getAttribute("id");
		CardinalityMin min = CardinalityMin.valueOf(e.getAttribute("min"));
		CardinalityMax max = CardinalityMax.valueOf(e.getAttribute("max"));
		NodeList cn = e.getChildNodes();
		IPredicate p=null;
		loop : for(int i=0;i<cn.getLength();i++)
		{
			if(cn.item(i) instanceof Element)
			{
				p = (IPredicate)f.fromXml((Element)(cn.item(0)));
				break loop;
			}
		}
		return new FollowAssertion(id,n,p,min,max);
	}

	public void setFilter(IPredicate<T> f) {
		this.f=f;
	}

	public IPredicate getFilter() {
		return f;
	}
}
