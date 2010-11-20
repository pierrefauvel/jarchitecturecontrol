package org.jarco.control.specifications.model;

import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jarco.code.external.ICodeElement;
import org.jarco.code.external.IXmlElement;
import org.jarco.control.specifications.ElementAndContext;
import org.jarco.control.specifications.itf.IConsequence;
import org.jarco.control.specifications.model.FM.kind;
import org.jarco.swing.tree.IExposableAsANode;
import org.jarco.xml.FromXmlFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ContextAffectationFromXpath<T extends ICodeElement> implements IConsequence<T>, IExposableAsANode {

	@FM(kind=kind.component)
	private String propertyName;
	@FM(kind=kind.component)
	private String str_expression;
	
	//pour l'éditeur graphique
	public ContextAffectationFromXpath()
	{
	}
	
	//new ContextAffectationFromXPath("classname","./@class")
	public ContextAffectationFromXpath( String propertyName, String xpath)
	{
		this.propertyName = propertyName;
		this.str_expression = xpath;
	}
	
	public void apply(T e, List list) {
		IXmlElement xml = (IXmlElement)e;
		String s = xml.getValueForXPath(str_expression);
		ElementAndContext ec = (ElementAndContext)(list.get(list.size()-1));
		System.out.println("PF74 "+propertyName+" affected as "+s);
		ec.setContextProperty(propertyName,s);
	}

	public String toXml() {
		StringBuffer sb=new StringBuffer();
		sb.append("<context-affectation-from-xpath context-property=\""+propertyName+"\">");
		sb.append("<xpath>"+str_expression+"</xpath>");
		sb.append("</context-affectation-from-xpath>");
		return sb.toString();
	}

	public static ContextAffectationFromXpath fromXml (FromXmlFactory f, Element e)
	{
		String pn = e.getAttribute("context-property");
		String xpath = null;
		NodeList nl = e.getChildNodes();
		loop: for (int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
				xpath = ((Element)(nl.item(i))).getTextContent();
				break loop;
			}
		}
		return new ContextAffectationFromXpath(pn,xpath);
	}
	
	public String toString()
	{
		return "context-affectation xpath:{name="+propertyName+",expression="+str_expression+"}";
	}
	public String toLabel()
	{
		return "<b>context-affectation xpath</b>:{<b>name</b>="+propertyName+",<b>expression</b>="+str_expression+"}";
	}

}
