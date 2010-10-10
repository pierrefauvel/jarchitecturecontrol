package org.jarco.control.specifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jarco.code.external.ICodeElement;
import org.jarco.collections.ImmutableMap;

public class ElementAndContext<T extends ICodeElement> {
	
	private T element;
	private Map<String,String> context;
	
	public ElementAndContext(T element)
	{
		this.element=element;
		context = new HashMap<String,String>();
	}
	
	public ElementAndContext(T element,ImmutableMap<String,String> av)
	{
		this.element=element;
		context = new HashMap<String,String>();
		for(String an : av.keys())
		{
			context.put(an,av.get(an));
		}
	}

	public T getElement()
	{
		return element;
	}
	public String getContextProperty(String pn)
	{
		return context.get(pn);
	}
	
	public String toString()
	{
		return element+ " "+ context;
	}
	
	public static String getContextPropertyInStack(List stack,String pn)
	{
		for (int i=0;i<stack.size();i++)
		{
			Object object = stack.get(stack.size()-1-i);
			if(object instanceof ElementAndContext)
			{
				ElementAndContext ce = (ElementAndContext)object;
				String pv = ce.getContextProperty(pn);
				if(pv!=null)
					return pv;
			}
		};
		throw new RuntimeException("PF59 Could not resolve context property "+pn+" in stack "+stack);
	}

	public ImmutableMap<String,String> subContext(ImmutableMap<String, String> m) {
		Map<String,String> rc=new HashMap<String,String>();
		for(String k : context.keySet())
			rc.put(k, context.get(k));
		for(String k : m.keys())
			rc.put(k, context.get(k));
		return new ImmutableMap<String,String>(rc);
	}

	public ImmutableMap<String,String> subContext(ElementAndContext ec1) {
		return subContext(new ImmutableMap<String,String>(ec1.context));
	}

	public void setContextProperty(String propertyName, String s) {
		context.put(propertyName,s);
	}

	public static String dumpContextPropertiesInStack(List stack) {
		StringBuffer sb=new StringBuffer();
		for (int i=0;i<stack.size();i++)
		{
			Object object = stack.get(stack.size()-1-i);
			if(object instanceof ElementAndContext)
			{
				ElementAndContext ce = (ElementAndContext)object;
				sb.append(ce.dumpProperties());
			}
		};
		return sb.toString();
	}
	
	String dumpProperties()
	{
		return context.toString();
	}
}
