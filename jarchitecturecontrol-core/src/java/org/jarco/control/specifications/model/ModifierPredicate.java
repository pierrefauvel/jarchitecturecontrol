package org.jarco.control.specifications.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.collections.ImmutableMap;
import org.jarco.control.specifications.itf.IPredicate;
import org.jarco.swing.components.FM;
import org.jarco.swing.components.IExposableAsANode;
import org.jarco.swing.components.FM.kind;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ModifierPredicate<T extends ICodeElement> implements IPredicate<T>{
	
	@FM(kind=kind.component)
	private EModifier[] _modifiers;
	
	public ModifierPredicate(EModifier... modifiers)
	{
		_modifiers =modifiers;
	}

	//pour l'éditeur graphique
	public ModifierPredicate()
	{
		
	}
	
	public ImmutableMap<String,String> include(T t) {
		ImmutableMap<String,String> RC_OK = new ImmutableMap(new HashMap<String,String>());
		if(t instanceof IClass)
		{	
			for(EModifier _modifier : _modifiers)
				if(!((IClass)t).getModifiers().contains(_modifier))
					return null;
		}
		else if(t instanceof IField)
		{
			for(EModifier _modifier : _modifiers)
				if(!((IField)t).getModifiers().contains(_modifier))
					return null;
		}
		else if(t instanceof IMethod)
		{
			for(EModifier _modifier : _modifiers)
				if(!((IMethod)t).getModifiers().contains(_modifier))
					return null;
		}
		else throw new RuntimeException("Unexpected type "+t.getClass().getName()+" in ModifierFilter");
		return RC_OK;
	}
	
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		for(EModifier _modifier : _modifiers)
			sb.append(_modifier+" ");
		return "modifiers{"+sb+"}";
	}

	public String toLabel()
	{
		StringBuffer sb=new StringBuffer();
		for(EModifier _modifier : _modifiers)
			sb.append(_modifier+" ");
		return "<b>modifiers</b>={"+sb+"}";
	}

	public String toXml()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<modifier-predicate>");
		for(EModifier e: _modifiers)
		{
			sb.append("<modifier value=\""+e+"\"/>");
		}
		sb.append("</modifier-predicate>");
		return sb.toString();
	}
//	public static ModifierPredicate fromXml(FromXmlFactory f, Element e)
	public void fromXml(FromXmlFactory f, Element e)
	{
		NodeList nl = e.getChildNodes();
		List<EModifier> m = new ArrayList<EModifier>();
		for (int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
				m.add( EModifier.valueOf(((Element)nl.item(i)).getAttribute("value")));
			}
		}
		_modifiers =m.toArray(new EModifier[]{});
	}
}
