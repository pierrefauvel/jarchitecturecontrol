package org.jarco.control.specifications;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.jarco.control.specifications.model.AnnotationPredicate;
import org.jarco.control.specifications.model.ChainedProductionRule;
import org.jarco.control.specifications.model.FollowAssertion;
import org.jarco.control.specifications.model.FollowProductionRule;
import org.jarco.control.specifications.model.ModifierPredicate;
import org.jarco.control.specifications.model.NamePredicate;
import org.jarco.control.specifications.model.OrAssertion;
import org.jarco.control.specifications.model.PredicateAssertion;
import org.jarco.control.specifications.model.Specification;
import org.jarco.control.specifications.model.TagAffectation;
import org.jarco.control.specifications.model.TagPredicate;
import org.jarco.tags.external.ITagRepository;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

//TODO V1.1 FromXmlFactory doit g�n�rer le sch�ma
public class FromXmlFactory {
	
	private ITagRepository tagRepo;
	
	
	private Map<String,Method> hm=new HashMap<String,Method>();
	
	private void register(Class c)
	{
		try
		{
			String cn = c.getSimpleName();
			StringBuffer sb = new StringBuffer();
			sb.append(Character.toLowerCase(cn.charAt(0)));
			for (int i=1;i<cn.length();i++)
			{
				char ch = cn.charAt(i);
				if(Character.isUpperCase(ch))
				{
					sb.append("-");
					sb.append(Character.toLowerCase(ch));
				}
				else
					sb.append(ch);
			}
			
			
		Method m = c.getDeclaredMethod("fromXml", new Class[]{FromXmlFactory.class,Element.class});
		if((m.getModifiers() & Modifier.STATIC)==0)
			throw new RuntimeException("registering a class "+c+" with a non static fromXml method");
		Class rt = m.getReturnType();
		if(!rt.isAssignableFrom(c))
			throw new RuntimeException("the fromXml method in "+c+" should return an instance of the type");
		hm.put(sb.toString(),m);
		}
		catch(NoSuchMethodException ex)
		{
			throw new RuntimeException("registering a class "+c+" without the fromXml method");
		}
	}
	
	public FromXmlFactory (ITagRepository tagRepo)
	{
		this.tagRepo=tagRepo;
		register(AnnotationPredicate.class);
		register(ChainedProductionRule.class);
		register(FollowAssertion.class);
		register(FollowProductionRule.class);
		register(ModifierPredicate.class);
		register(NamePredicate.class);
		register(OrAssertion.class);
		register(PredicateAssertion.class);
		register(Specification.class);
		register(TagAffectation.class);
		register(TagPredicate.class);
	}

	public String dump(Element e)
	{
		StringBuffer sb=new StringBuffer();
		sb.append(e.getNodeName());
		sb.append(" ");
		NamedNodeMap nnm = e.getAttributes();
		for(int i=0;i<nnm.getLength();i++)
		{
			Node at = nnm.item(i);
			sb.append(at.getNodeName()+"="+at.getNodeValue()+" ");
		};
		return sb.toString();
	}
	
	public Object fromXml(Element ei) {
		try
		{
			Method m = hm.get(ei.getNodeName());
			if(m==null)
				throw new RuntimeException("Could not find fromXml class for name "+ei.getNodeName());
			return m.invoke(null, new Object[]{this,ei});
		}
		catch(Throwable t)
		{
			throw new RuntimeException("Pb while calling fromXml on "+dump(ei),t);
		}
	}

	public ITagRepository getTagRepository() {
		return tagRepo;
	}
}