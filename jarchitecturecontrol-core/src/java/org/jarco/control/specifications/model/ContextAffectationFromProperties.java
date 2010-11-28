package org.jarco.control.specifications.model;

import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IPropertiesDocument;
import org.jarco.code.external.IXmlElement;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.persistence.FromXmlFactory;
import org.jarco.swing.tree.IExposableAsANode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ContextAffectationFromProperties<T extends ICodeElement> implements IConsequence<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String propertyName;
	@FM(kind=kind.component)
	private String key;
	
	//pour l'éditeur graphique
	public ContextAffectationFromProperties()
	{
		
	}
	
	//new ContextAffectationFromXPath("classname","./@class")
	public ContextAffectationFromProperties( String propertyName, String key)
	{
		this.propertyName = propertyName;
		this.key=key;
	}
	
	public void apply(T e, List list) {
		IPropertiesDocument props = (IPropertiesDocument)e;
		String pv = props.get(key);
		ElementAndContext ec = (ElementAndContext)(list.get(list.size()-1));
		ec.setContextProperty(propertyName,pv);
	}

	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<context-affectation-from-properties context-property=\""+propertyName+"\" key=\""+ key+"\">");
		sb.append("</context-affectation-from-properties>");
		return sb.toString();
	}

	public static ContextAffectationFromProperties fromXml (FromXmlFactory f, Element e)
	{
		String pn = e.getAttribute("context-property");
		String pk = e.getAttribute("key");
		return new ContextAffectationFromProperties(pn,pk);
	}
	
	public String toString()
	{
		return "context-affectation properties:{name="+propertyName+",key="+key+"}";
	}
	public String toLabel()
	{
		return "<b>context-affectation properties</b>:{<b>name</b>="+propertyName+",<b>key</b>="+key+"}";
	}
}
