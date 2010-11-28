package org.jarco.control.specifications.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.itf.IProductionRule;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ChainedProductionRule<T extends ICodeElement> implements IProductionRule<T>,IExposableAsANode {

	@FM(kind=kind.treenode)
	private List<IProductionRule<?>> rules=new ArrayList<IProductionRule<?>>();
	
	public ChainedProductionRule()
	{
	}
	
	public ChainedProductionRule<T> chain(IProductionRule<?> r)
	{
		rules.add(r);
		return this;
	}
	
	public ImmutableList rules(){
		return new ImmutableList(rules);
	}
	
	public List<ElementAndContext<T>> produce(Object aContext, IPredicate<T> filter, List lst) {
		List<ElementAndContext<?>> wave = new ArrayList<ElementAndContext<?>>();
		wave.add(new ElementAndContext((ICodeElement)aContext));
		for(IProductionRule<?> ri : rules)
		{
			wave = wave(wave,ri,lst);
		};
		List<ElementAndContext<T>> rc = new ArrayList<ElementAndContext<T>>();
		for(ElementAndContext<?> e : wave)
		{
			ImmutableMap<String,String> m = filter==null ? null : filter.include((T)e.getElement());
			if(filter==null || m!=null)
			{
				rc.add(new ElementAndContext(e.getElement(),e.subContext(m==null ? new ImmutableMap<String,String>(new HashMap<String,String>()) : m)));
			}
		}
		return rc;
	}
	
	private List<ElementAndContext<?>> wave(List<ElementAndContext<?>> wave, IProductionRule<?> ri, List lst)
	{
		List<ElementAndContext<?>> rc= new ArrayList<ElementAndContext<?>> ();
		for(ElementAndContext<?> ec:wave)
		{
			try
			{
				List r = ri.produce(ec.getElement(), null,lst);
				for(Object i : r)
				{
					ElementAndContext<?> ec1 = (ElementAndContext<?>)i;
					rc.add(new ElementAndContext(ec1.getElement(),ec.subContext(ec1)));
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace(System.err);
			}
		};
		return rc;
	}

	public String toString()
	{
		return "ChainedProductionRule {"+rules+"}";
	}
	
	public String toLabel()
	{
		return "<html><b>chained production rules</b></html>";
	}
	
	public String toXml()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<chained-production-rule>");
		for(IProductionRule i : rules)
		{
			sb.append(i.toXml());
		}
		sb.append("</chained-production-rule>");
		return sb.toString();
	}
	
	public static ChainedProductionRule fromXml(FromXmlFactory f,Element e)
	{
		ChainedProductionRule cpr = new ChainedProductionRule();
		NodeList nl = e.getChildNodes();
		for(int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
			Element ei = (Element)(nl.item(i));
			IProductionRule pr = (IProductionRule)(f.fromXml(ei));
			cpr=cpr.chain(pr);
			}
		};
		return cpr;
	}

	public void remove(IProductionRule prFils) {
		rules.remove(prFils);
	}
}
