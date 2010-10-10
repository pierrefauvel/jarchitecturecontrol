package org.jarco.control.specifications.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.INamed;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.FromXmlFactory;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.IExposableAsANode;
import org.w3c.dom.Element;

public class NamePredicate<T extends ICodeElement> implements IPredicate<T>, IExposableAsANode {

//	private String p;
//	private Matcher m;
	
	@FM(kind=kind.component)
	private String prefix;
	@FM(kind=kind.component)
	private String variableName;
	@FM(kind=kind.component)
	private String suffix;
	@FM(kind=kind.component)
	private String exactMatch;
	
	//TODO V1.1 Implémenter les name filter avec des regexp
	
	//pour l'éditeur graphique
	public NamePredicate()
	{
		
	}
	
	public NamePredicate(String exactMatch)
	{
		this.exactMatch = exactMatch;
	}
	
	public NamePredicate(String prefix, String variableName, String suffix)
	{
//		p=pattern;
		//1 construire le matcher
		//2 préparer la récupération des valeurs obtenues
		//3 revoir l'interface (on doit pouvoir modifier le contexte pour positionner les valeurs obtenues

		this.prefix=prefix;
		this.variableName=variableName;
		this.suffix=suffix;
	}
	
	public ImmutableMap<String,String> include(T t) {
		String n = t.getName();
		
		if(exactMatch!=null && exactMatch.trim().length()>0)
		{
			if(exactMatch.compareTo(n)==0)
			{
			Map<String,String> m = new HashMap<String,String>();
			return new ImmutableMap<String,String>(m);
			}
//			System.out.println("PF80 exact match ko, looking for "+exactMatch+", found "+n);
			return null;
		}
		
		if(prefix.trim().length()>0 && !(n.startsWith(prefix)))
			return null;
		if(suffix.trim().length()>0 && !(n.endsWith(suffix)))
			return null;
		
//		System.out.println("PF60 Ok for "+n+" prefix="+prefix+", suffix="+suffix);
		
		Map<String,String> m = new HashMap<String,String>();
		m.put(variableName, n.substring(prefix.length(),n.length()-suffix.length()));
		return new ImmutableMap<String,String>(m);
	}

	public String toString()
	{
		if(exactMatch!=null)
			return "named "+exactMatch;
		return "named "+prefix+"{"+variableName+"}"+suffix;
	}
	
	public String toLabel()
	{
		if(exactMatch!=null)
			return "<b>exactMatch</b>="+exactMatch;
		return "<b>prefix</b>="+prefix+" <b>variable-name</b>="+variableName+" <b>suffix</b>="+suffix;
	}
	
	public String toXml()
	{
		if(exactMatch!=null && exactMatch.trim().length()>0)
			return "<name-predicate name=\""+exactMatch+"\" />";
		else
			return "<name-predicate name-prefix=\""+prefix+"\" variable-name=\""+variableName+"\" suffix=\""+suffix+"\" />";
	}
	
	public static NamePredicate fromXml(FromXmlFactory f, Element e)
	{
		String name = e.getAttribute("name");
		if(name!=null && name.length()>0)
		{
			return new NamePredicate(name);
		}
		else
		{
			String name_prefix = e.getAttribute("name-prefix");
			String variable_name = e.getAttribute("variable-name");
			String suffix = e.getAttribute("suffix");
			return new NamePredicate(name_prefix,variable_name,suffix);
		}
	}
}
