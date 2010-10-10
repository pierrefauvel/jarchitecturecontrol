package org.jarco.code.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IXmlElement;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedSet;
import org.jarco.tags.external.ITag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlElementInternal extends ACodeElementInternal implements IXmlElement {

	private Element e;
	
	public XmlElementInternal (ICodeRepository repo,Element e)
	{
		super(repo);
		this.e=e;
	}
	
	public String getName() {
		return e.getNodeName();
	}

	public ImmutableList<IXmlElement> getChildNodes(){
		List<IXmlElement> rc = new ArrayList<IXmlElement>();
		NodeList nl = e.getChildNodes();
		for (int i=0;i<nl.getLength();i++)
		{
			if(nl.item(i) instanceof Element)
			{
				rc.add(new XmlElementInternal(getRepository(),(Element)nl.item(i)));
			}
		};
		return new ImmutableList<IXmlElement>(rc);
	}
	
	public ImmutableList<IXmlElement> getChildNodesForXPath(String xpath){
		try
		{
			XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression xpe = xp.compile(xpath);
			NodeList nl = (NodeList)(xpe.evaluate(e,XPathConstants.NODESET));
			System.out.println("PF72 xpath="+xpath+", found="+nl.getLength());
			
			List<IXmlElement> rc = new ArrayList<IXmlElement>();
			for (int i=0;i<nl.getLength();i++)
			{
				if(nl.item(i) instanceof Element)
				{
					rc.add(new XmlElementInternal(getRepository(),(Element)nl.item(i)));
				}
			};
			return new ImmutableList<IXmlElement>(rc);
		} catch (XPathExpressionException e) {
			System.err.println("Pb in getChildNodesForXPath "+xpath);
			e.printStackTrace(System.err);
			return new ImmutableList<IXmlElement>(new ArrayList<IXmlElement>());
		}
	}

	public String getValueForXPath(String xpath) {
		try
		{
			XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression xpe = xp.compile(xpath);
			String str = (String)(xpe.evaluate(e,XPathConstants.STRING));
			return str;
		} catch (XPathExpressionException e) {
			System.err.println("Pb in getValueForXPath "+xpath);
			e.printStackTrace(System.err);
			return "?";
		}
	}
}
